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
import br.com.gaustitch.mapper.DozerMapper;
import br.com.gaustitch.model.Option;
import br.com.gaustitch.model.Topic;
import br.com.gaustitch.repositories.TopicsRepository;

@Service
public class TopicsServices {
	
	private Logger logger = Logger.getLogger(TopicsServices.class.getName());
	
	@Autowired
    private TopicsRepository topicRepository;

	@Transactional
    public TopicVO create(TopicVO topicVO) {
        logger.info("Creating topic with title: "+ topicVO.getTitle());
        Topic topic = DozerMapper.toTopic(topicVO);
        // Verificar se o tópico é privado e definir a senha
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
        logger.info("Adding options to topic");
        Topic topic = topicRepository.findById(topicId)
            .orElseThrow(() -> {
                return new RuntimeException("Topic not found");
            });

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
