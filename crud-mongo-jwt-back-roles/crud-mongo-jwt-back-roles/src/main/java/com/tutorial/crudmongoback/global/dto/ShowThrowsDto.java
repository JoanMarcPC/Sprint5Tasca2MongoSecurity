package com.tutorial.crudmongoback.global.dto;



import java.lang.reflect.Array;
import java.util.ArrayList;
import com.tutorial.crudmongoback.security.entity.Throw;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShowThrowsDto {

    private ArrayList<Throw> _throws;

}
