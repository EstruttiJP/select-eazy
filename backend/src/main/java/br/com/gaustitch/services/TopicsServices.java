package br.com.gaustitch.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gaustitch.data.vo.v1.OptionVO;
import br.com.gaustitch.data.vo.v1.TopicVO;
import br.com.gaustitch.exceptions.RequiredObjectIsNullException;
import br.com.gaustitch.exceptions.ResourceNotFoundException;
import br.com.gaustitch.mapper.DozerMapper;
import br.com.gaustitch.model.Option;
import br.com.gaustitch.model.Topic;
import br.com.gaustitch.repositories.OptionRepository;
import br.com.gaustitch.repositories.TopicsRepository;

@Service
public class TopicsServices {
	
	private Logger logger = Logger.getLogger(TopicsServices.class.getName());
	
	@Autowired
    private TopicsRepository topicRepository;
	
	@Autowired
    private OptionRepository optionRepository;

	@Transactional
    public TopicVO create(TopicVO topicVO) {
        logger.info("Creating topic with title: "+ topicVO.getTitle());
        Topic topic = DozerMapper.toTopic(topicVO);
        // Verificar se o tópico é privado
        if (topicVO.isPrivate()) {
            topic.setPassword(topicVO.getPassword());
        } else {
            topic.setPassword(null);
        }
        topic = topicRepository.save(topic);
        TopicVO result = DozerMapper.toTopicVO(topic);
        logger.info("Topic created with id: "+ result.getId());
        return result;
    }

	public List<TopicVO> findAll() {
        logger.info("Fetching all topics");
        List<Topic> topics = topicRepository.findAll();
        List<TopicVO> topicVOList = topics.stream()
            .map(DozerMapper::toTopicVOWithoutPassword)
            .collect(Collectors.toList());
        return topicVOList;
    }
    
	@Transactional
    public TopicVO addOptions(Long topicId, List<OptionVO> optionVOs) {
		if (optionVOs == null) throw new RequiredObjectIsNullException();
        logger.info("Adding options to topic");
        Topic topic = topicRepository.findById(topicId)
        		.orElseThrow(() -> new ResourceNotFoundException("Topic not found!"));

        List<Option> options = new ArrayList<>();
        for (OptionVO optionVO : optionVOs) {
            Option option = DozerMapper.parseObject(optionVO, Option.class);
            option.setTopic(topic);
            options.add(option);
        }

        topic.getOptions().addAll(options);
        topic = topicRepository.save(topic);
        
        return DozerMapper.toTopicVO(topic);
    }
	
	public OptionVO updateOption(OptionVO optionVO) {
		if (optionVO == null) throw new RequiredObjectIsNullException();
	    logger.info("Updating option with ID: "+ optionVO.getId());
	    Option option = optionRepository.findById(optionVO.getId())
	    		.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
	    option.setId(optionVO.getId());
	    option.setName(optionVO.getName());
	    option.setImageUrl(optionVO.getImageUrl());
	    option = optionRepository.save(option);

	    return DozerMapper.parseObject(option, OptionVO.class);
	}
	 
	public void deleteOption(Long optionId) {
	    logger.info("Deleting option with ID "+ optionId);
	    Option option = optionRepository.findById(optionId)
	    		.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
	    optionRepository.delete(option);
	}

	
	// public String encryptPassword(String password) {
    //     Map<String, PasswordEncoder> encoders = new HashMap<>();

    //     Pbkdf2PasswordEncoder pbkdf2Encoder
    //             = new Pbkdf2PasswordEncoder(
    //                     "", 8, 185000,
    //                     SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

    //     encoders.put("pbkdf2", pbkdf2Encoder);
    //     DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
    //     passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);

    //     String encryptedPassword = passwordEncoder.encode(password);

    //     return removePrefixSuffix(encryptedPassword);
    // }
	
	// public static String removePrefixSuffix(String password) {
    //     if (password != null && password.startsWith("{pbkdf2}")) {
    //         return password.substring("{pbkdf2}".length());
    //     }
    //     return password;
    // }
}
