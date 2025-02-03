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
        topic = topicRepository.save(topic);
        TopicVO result = DozerMapper.toTopicVO(topic);
        logger.info("Topic created with id: "+ result.getId());
        return result;
    }

    public List<TopicVO> findAll() {
        logger.info("Fetching all topics");
        List<Topic> topics = topicRepository.findAll();
        List<TopicVO> topicVOList = topics.stream()
            .map(DozerMapper::toTopicVO)
            .collect(Collectors.toList());
        logger.info("Fetched "+topicVOList.size()+" topics");
        return topicVOList;
    }

    @Transactional
    public TopicVO addOptions(Long topicId, List<OptionVO> optionVOs) {
        logger.info("Adding options to topic with id: "+ topicId);
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
        
        logger.info("Options added to topic with id: "+ topicId);
        return DozerMapper.toTopicVO(topic);
    }
}
