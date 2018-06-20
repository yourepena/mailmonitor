package br.com.bluesi.mailmonitor.entities;
// Generated 01/09/2017 00:05:12 by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TagsBlueOcorrenciaId generated by hbm2java
 */
@Embeddable
public class TagsBlueOcorrenciaId implements java.io.Serializable {

	private int tagBlueIdFk;
	private int ocorrenciaIdFk;

	public TagsBlueOcorrenciaId() {
	}

	public TagsBlueOcorrenciaId(int tagBlueIdFk, int ocorrenciaIdFk) {
		this.tagBlueIdFk = tagBlueIdFk;
		this.ocorrenciaIdFk = ocorrenciaIdFk;
	}

	@Column(name = "tag_blue_id_fk", nullable = false)
	public int getTagBlueIdFk() {
		return this.tagBlueIdFk;
	}

	public void setTagBlueIdFk(int tagBlueIdFk) {
		this.tagBlueIdFk = tagBlueIdFk;
	}

	@Column(name = "ocorrencia_id_fk", nullable = false)
	public int getOcorrenciaIdFk() {
		return this.ocorrenciaIdFk;
	}

	public void setOcorrenciaIdFk(int ocorrenciaIdFk) {
		this.ocorrenciaIdFk = ocorrenciaIdFk;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TagsBlueOcorrenciaId))
			return false;
		TagsBlueOcorrenciaId castOther = (TagsBlueOcorrenciaId) other;

		return (this.getTagBlueIdFk() == castOther.getTagBlueIdFk())
				&& (this.getOcorrenciaIdFk() == castOther.getOcorrenciaIdFk());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getTagBlueIdFk();
		result = 37 * result + this.getOcorrenciaIdFk();
		return result;
	}

}
