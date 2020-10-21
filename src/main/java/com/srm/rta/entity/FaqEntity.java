
package com.srm.rta.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * 
 * @author Priya [SRM]
 */
@Data
@Entity
@Table(name = "faq")
@XmlRootElement
public class FaqEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "FAQ_ID")
	private Integer faqId;
	@Column(name = "QUESTION")
	private String question;
	@Column(name = "ANSWER")
	private String answer;
	@Column(name = "rin_ma_entity_id")
	private Integer entityLicenseId;
	@Column(name = "delete_flag")
	private Character deleteFlag;
}
