package br.com.gaustitch.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

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
        List<String> optionNames = topic.getOptions().stream()
            .map(Option::getName)
            .collect(Collectors.toList());
        topicVO.setOptions(optionNames);  // Set options as names
        return topicVO;
    }

    public static Topic toTopic(TopicVO topicVO) {
        Topic topic = mapper.map(topicVO, Topic.class);
        if (topicVO.getOptions() != null) {
            List<Option> options = topicVO.getOptions().stream()
                .map(name -> {
                    Option option = new Option();
                    option.setName(name);
                    option.setTopic(topic);
                    return option;
                }).collect(Collectors.toList());
            topic.setOptions(options);
        }
        return topic;
    }
}
