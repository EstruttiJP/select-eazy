package br.com.gaustitch.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gaustitch.data.vo.v1.OptionVO;
import br.com.gaustitch.data.vo.v1.TopicVO;
import br.com.gaustitch.services.TopicsServices;

@RestController
@RequestMapping("/api/options/v1")
public class OptionsController {

	@Autowired
	private TopicsServices topicsServices;
	
    @PostMapping("/topic/{topicId}")
    public ResponseEntity<TopicVO> addOptions(@PathVariable Long topicId, @RequestBody List<OptionVO> options) {
        TopicVO updatedTopic = topicsServices.addOptions(topicId, options);
        return new ResponseEntity<>(updatedTopic, HttpStatus.OK);
    }
    
    @PutMapping
    public ResponseEntity<OptionVO> updateOption(@RequestBody OptionVO optionVO) {
        OptionVO updatedOption = topicsServices.updateOption(optionVO);
        return new ResponseEntity<>(updatedOption, HttpStatus.OK);
    }
    
    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long optionId) {
        topicsServices.deleteOption(optionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
