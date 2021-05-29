package br.com.devcave.reactive.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;

@Data
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Anime {
    @Id
    private Long id;

    @NotBlank
    private String name;
}
