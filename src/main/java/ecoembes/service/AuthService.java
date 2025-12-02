package ecoembes.service;

import java.util.HashMap;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ecoembes.dao.EmployeeRepository;
import ecoembes.entity.Employee;

@Service
public class AuthService {
	private static final Logger log = LoggerFactory.getLogger(AuthService.class);
	
	// Simulating a user repository
	private final EmployeeRepository employeeRepository;
    
    // Storage to keep the session of the users that are logged in
    private static Map<String, Employee> tokenStore = new HashMap<>();
    
    public AuthService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Login method that checks if the user exists in the database and validates the password and assigns token
    public Optional<String> login(String email, String password) {
    	Optional<Employee> user = employeeRepository.findByEmail(email);
        
        if (user.isPresent() && user.get().checkPassword(password)) {
        	String token = generateToken();  	   // Generate a random token for the session
            tokenStore.put(token, user.get());     // Store the token and associate it with the user

            return Optional.of(token);
        } else {
        	log.warn("User not found or invalid password.");
        	return Optional.empty();
        }
    }
    
    // Logout method to remove the token from the session store
    public Optional<Boolean> logout(String token) {
        if (tokenStore.containsKey(token)) {
            tokenStore.remove(token);
            return Optional.of(true);
        } else {
        	log.warn("Invalid token for logout.");
            return Optional.empty();
        }
    }   

    // Method to get the user based on the token
    public Employee getUserByToken(String token) {
        return tokenStore.get(token); 
    }

    // Synchronized method to guarantee unique token generation
    private static synchronized String generateToken() {
        return Long.toHexString(System.currentTimeMillis());
    }
    public static Employee validateToken(String token) {
		if (tokenStore.containsKey(token)) {
			return tokenStore.get(token);
		} else {
			return null;
		}
	}
}

