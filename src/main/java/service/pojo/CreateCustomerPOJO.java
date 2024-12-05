package service.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@With
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateCustomerPOJO {
    public String name;
    public String phone;
    public AddParam addParam;
}
