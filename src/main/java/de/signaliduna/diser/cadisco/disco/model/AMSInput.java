package de.signaliduna.diser.cadisco.disco.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Der Input vom AMS-System (extern).
 * Entspricht dem Gast, der an der Disco eintrifft.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AMSInput {

    private String globalId;
    private String name;
    private String sparte;
    private List<String> contractNumbers;
}
