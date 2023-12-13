//Group23
package ca.gbc.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @Id
    private String Id;
    private String name;

}