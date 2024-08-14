package Paywise.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import Paywise.entity.Client;
import Paywise.entity.Messages;
import Paywise.entity.User;
import Paywise.exception.ResourceNotFoundException;
import Paywise.repository.MessagesRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Paywise.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

	public UserController(Client client){
		this.client = client;
	}

	@Autowired
	private Client client;
	@Autowired
	private RabbitTemplate template;

	@Autowired
	private MessagesRepository MessagesRepository;

	@Autowired
	private UserRepository userRepository;

	// get all users
	@GetMapping
	public List<User> getAllUsers() {
		return this.userRepository.findAll();
	}

	// get user by id
	@GetMapping("/{id}")
	public User getUserById(@PathVariable (value = "id") long userId) {
		return this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
	}

	@PostMapping("/uploadMessage")
	public Messages uploadRabbitMQ(@RequestBody String data) throws Exception {

		try{
			data = data.replace("+", " ");
			data = data.replace("=", " ");
			String st = client.encrypt(data);
		//	 template.convertAndSend("Test","Ron",st);
			 Messages message = new Messages(st, LocalDateTime.now());
			return this.MessagesRepository.save(message);}
		catch (Exception ex){
			System.out.println(ex);
			return null;
		}

	}
	@PostMapping("/dec")
	public String decrypt(@RequestBody String encryptedData) throws Exception {
		return client.decrypt(encryptedData);
	}

	@GetMapping("/messages/{id}")
	public Optional<Messages> getMessage(@PathVariable ("id") long messageId) {
		return this.MessagesRepository.findById(messageId);
	}

	// create user
	@PostMapping
	public User createUser(@RequestBody User user) {
		return this.userRepository.save(user);
	}
	
	// update user
	@PutMapping("/{id}")
	public User updateUser(@RequestBody User user, @PathVariable ("id") long userId) {
		 User existingUser = this.userRepository.findById(userId)
			.orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
		 existingUser.setFirstName(user.getFirstName());
		 existingUser.setLastName(user.getLastName());
		 existingUser.setEmail(user.getEmail());
		 return this.userRepository.save(existingUser);
	}
	// delete user by id
	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable ("id") long userId){
		 User existingUser = this.userRepository.findById(userId)
					.orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
		 this.userRepository.delete(existingUser);
		 return ResponseEntity.ok().build();
	}
}
