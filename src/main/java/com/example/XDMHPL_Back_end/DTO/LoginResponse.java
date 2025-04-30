package com.example.XDMHPL_Back_end.DTO;

public class LoginResponse {
    private String sessionId;
    private int userId;
    private String username;
    private String role;
    private String message;
    public LoginResponse(String sessionId, int userId, String username, String role, String message) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.message = message;
    }

    // getters & setters
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}