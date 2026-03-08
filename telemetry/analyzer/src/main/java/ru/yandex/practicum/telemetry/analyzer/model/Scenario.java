package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scenarios")
public class Scenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hubId;

    private String name;

    @JoinTable(
            name = "scenario_conditions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_id")
    )
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKeyColumn(table = "scenario_conditions", name = "sensor_id")
    private Map<String, Condition> conditions = new HashMap<>();


    @JoinTable(
            name = "scenario_actions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id")
    )
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @MapKeyColumn(table = "scenario_actions", name = "sensor_id")
    private Map<String, Action> actions = new HashMap<>();
}
