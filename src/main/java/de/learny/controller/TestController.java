package de.learny.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.learny.controller.exception.NotEnoughPermissionsException;
import de.learny.controller.exception.ResourceNotFoundException;
import de.learny.dataaccess.TestRepository;
import de.learny.domain.Account;
import de.learny.domain.Subject;
import de.learny.domain.Test;
import de.learny.security.service.LoggedInAccountService;

@RestController
@RequestMapping("/api/tests")
public class TestController {
	
	@Autowired
	private TestRepository testRepository;
	
	@Autowired
	private LoggedInAccountService userToAccountService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	Iterable<Test> getAllTests(){
		return testRepository.findAll();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	Test getTest(@PathVariable("id") long id){
		Test test = testRepository.findById(id);
		if(test == null)
			throw new ResourceNotFoundException("Ein Test mit diese id existiert nicht");
		return test;
	}

	public TestRepository getTestRepository() {
		return testRepository;
	}

	public void setTestRepository(TestRepository testRepository) {
		this.testRepository = testRepository;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	void delete(@PathVariable("id") long id){
		Test test = testRepository.findById(id);
		if(test == null)
			throw new ResourceNotFoundException("Ein Test mit diese id existiert nicht");
		if(permitted(id)){
			this.testRepository.delete(id);
		}
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.PUT, consumes={MediaType.APPLICATION_JSON_VALUE})
	void update(@PathVariable("id") long id, @RequestBody Test test){
		//TODO: Muss noch implemntiert werden
	}
	
	@RequestMapping(value = "/{id}/questions", method = RequestMethod.GET)
	void getAllQuestionsToTest(){
		//TODO: Muss noch implemtiert werden
	}
	
	@RequestMapping(value = "/{id}/results", method = RequestMethod.GET)
	void getResultsFromTest(@PathVariable("id") long id){
		//TODO: Muss noch implemtiert werden
	}
	
	@RequestMapping(value = "/{id}/results", method=RequestMethod.POST, consumes={MediaType.APPLICATION_JSON_VALUE})
	void turnTest(@PathVariable("id") long id, @RequestBody Test test){
		//TODO: Muss noch implemntiert werden
	}
	
	@RequestMapping(value = "/{id}/highscore", method = RequestMethod.GET)
	void getHighscoreFromTest(@PathVariable("id") long id){
		//TODO: Muss noch implemtiert werden
	}
	
	private boolean permitted(long id){
		// Übeprüft ob Subject vorhaden
		Test test = testRepository.findById(id);
		// Überprüft ob Account Admin oder verantwortlich für Subject
		Account loggedInAccount = userToAccountService.getLoggedInAccount();
		boolean inCharge = false;
		Subject subject = test.getSubject();
		inCharge = subject.getAccountsInCharge().contains(loggedInAccount);
		if (inCharge || loggedInAccount.hasRole("admin")) {
			return true;
		} else {
			throw new NotEnoughPermissionsException("Nicht genug Rechte, um das auszuführen.");
		}
	}
}
