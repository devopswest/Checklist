package com.pwc.assurance.adc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.ChecklistTemplateStatus;

/**
 * LibraryTODO: Matrix vs Client metaadaTODO: DisclosureRequirements help or guidance fields.Main/core checlists, Supplemental ChecklistTemplates
 * 
 */
@ApiModel(description = ""
    + "LibraryTODO: Matrix vs Client metaadaTODO: DisclosureRequirements help or guidance fields.Main/core checlists, Supplemental ChecklistTemplates"
    + "")
@Entity
@Table(name = "checklist_template")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "checklisttemplate")
public class ChecklistTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private String version;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChecklistTemplateStatus status;

    @OneToMany(mappedBy = "checklistTemplate")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DisclosureRequirement> disclosureRequirements = new HashSet<>();

    @ManyToOne
    private Taxonomy territory;

    @ManyToOne
    private Taxonomy formType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ChecklistTemplate name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public ChecklistTemplate description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public ChecklistTemplate version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ChecklistTemplateStatus getStatus() {
        return status;
    }

    public ChecklistTemplate status(ChecklistTemplateStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ChecklistTemplateStatus status) {
        this.status = status;
    }

    public Set<DisclosureRequirement> getDisclosureRequirements() {
        return disclosureRequirements;
    }

    public ChecklistTemplate disclosureRequirements(Set<DisclosureRequirement> disclosureRequirements) {
        this.disclosureRequirements = disclosureRequirements;
        return this;
    }

    public ChecklistTemplate addDisclosureRequirement(DisclosureRequirement disclosureRequirement) {
        disclosureRequirements.add(disclosureRequirement);
        disclosureRequirement.setChecklistTemplate(this);
        return this;
    }

    public ChecklistTemplate removeDisclosureRequirement(DisclosureRequirement disclosureRequirement) {
        disclosureRequirements.remove(disclosureRequirement);
        disclosureRequirement.setChecklistTemplate(null);
        return this;
    }

    public void setDisclosureRequirements(Set<DisclosureRequirement> disclosureRequirements) {
        this.disclosureRequirements = disclosureRequirements;
    }

    public Taxonomy getTerritory() {
        return territory;
    }

    public ChecklistTemplate territory(Taxonomy taxonomy) {
        this.territory = taxonomy;
        return this;
    }

    public void setTerritory(Taxonomy taxonomy) {
        this.territory = taxonomy;
    }

    public Taxonomy getFormType() {
        return formType;
    }

    public ChecklistTemplate formType(Taxonomy taxonomy) {
        this.formType = taxonomy;
        return this;
    }

    public void setFormType(Taxonomy taxonomy) {
        this.formType = taxonomy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChecklistTemplate checklistTemplate = (ChecklistTemplate) o;
        if(checklistTemplate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, checklistTemplate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChecklistTemplate{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", version='" + version + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
