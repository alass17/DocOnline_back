package SoutenanceBackend.soutenance.DtoViewModels.Responses;

public class ApiResponse {
    private Boolean success;
    private String message;

    // Constructors
    public ApiResponse() {}

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    // Getters & Setters.
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
