package travel.plan.api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginParam {
    @NotBlank
    String userId;

    @NotBlank
    String password;
}
