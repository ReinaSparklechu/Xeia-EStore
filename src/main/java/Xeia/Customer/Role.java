package Xeia.Customer;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

// TODO: 30/8/2022 add roles
@Data
@RequiredArgsConstructor
public class Role {
    @NonNull
    private String name;
    @NonNull
    private long id;
}
