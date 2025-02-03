package br.com.gaustitch.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gaustitch.data.vo.v1.OptionVO;
import br.com.gaustitch.data.vo.v1.TopicVO;
import br.com.gaustitch.services.TopicsServices;

@RestController
@RequestMapping("/api/topics/v1")
public class TopicsController {

	@Autowired
	private TopicsServices topicsServices;
	
	@PostMapping
    public ResponseEntity<TopicVO> create(@RequestBody TopicVO topicVO) {
        TopicVO createdTopic = topicsServices.create(topicVO);
        return new ResponseEntity<>(createdTopic, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TopicVO>> findAll() {
        List<TopicVO> topics = topicsServices.findAll();
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }
    
    @PostMapping("/options/{topicId}")
    public ResponseEntity<TopicVO> addOptions(@PathVariable Long topicId, @RequestBody List<OptionVO> options) {
        TopicVO updatedTopic = topicsServices.addOptions(topicId, options);
        return new ResponseEntity<>(updatedTopic, HttpStatus.OK);
    }
}
