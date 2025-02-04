package br.com.gaustitch.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import br.com.gaustitch.data.vo.v1.OptionVO;
import br.com.gaustitch.data.vo.v1.TopicVO;
import br.com.gaustitch.model.Option;
import br.com.gaustitch.model.Topic;

public class DozerMapper {
	
	private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
        List<D> destinationObjects = new ArrayList<>();
        for (O o : origin) {
            destinationObjects.add(mapper.map(o, destination));
        }
        return destinationObjects;
    }
    
    public static TopicVO toTopicVO(Topic topic) {
        TopicVO topicVO = mapper.map(topic, TopicVO.class);
        List<OptionVO> optionVOs = new ArrayList<>();
        if (topic.getOptions() != null) {
            optionVOs = topic.getOptions().stream()
                .map(option -> {
                    OptionVO optionVO = new OptionVO();
                    optionVO.setName(option.getName());
                    optionVO.setImageUrl(option.getImageUrl());
                    return optionVO;
                })
                .collect(Collectors.toList());
        }
        topicVO.setOptions(optionVOs);  // Set options as OptionVO
        return topicVO;
    }

    public static TopicVO toTopicVOWithoutPassword(Topic topic) {
        TopicVO topicVO = toTopicVO(topic);
        topicVO.setPassword(null); // Remover a senha
        return topicVO;
    }

    public static Topic toTopic(TopicVO topicVO) {
        Topic topic = mapper.map(topicVO, Topic.class);
        if (topicVO.getOptions() != null) {
            List<Option> options = topicVO.getOptions().stream()
                .map(optionVO -> {
                    Option option = new Option();
                    option.setName(optionVO.getName());
                    option.setImageUrl(optionVO.getImageUrl());
                    option.setTopic(topic);
                    return option;
                }).collect(Collectors.toList());
            topic.setOptions(options);
        } else {
            topic.setOptions(new ArrayList<>());
        }
        return topic;
    }
}
