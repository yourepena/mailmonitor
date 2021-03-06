package br.com.bluesi.mailmonitor.entities;
// Generated 01/09/2017 00:05:12 by Hibernate Tools 5.2.3.Final

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TagsBlueOcorrencia generated by hbm2java
 */
@Entity
@Table(name = "tags_blue_ocorrencia",catalog = "opsblue")
public class TagsBlueOcorrencia implements java.io.Serializable {

	private TagsBlueOcorrenciaId id;
	private Ocorrencia ocorrencia;
	private TagBlue tagBlue;
	private Usuario usuario;
	private Date dataAssociacao;

	public TagsBlueOcorrencia() {
	}

	public TagsBlueOcorrencia(TagsBlueOcorrenciaId id, Ocorrencia ocorrencia, TagBlue tagBlue) {
		this.id = id;
		this.ocorrencia = ocorrencia;
		this.tagBlue = tagBlue;
	}

	public TagsBlueOcorrencia(TagsBlueOcorrenciaId id, Ocorrencia ocorrencia, TagBlue tagBlue, Usuario usuario,
			Date dataAssociacao) {
		this.id = id;
		this.ocorrencia = ocorrencia;
		this.tagBlue = tagBlue;
		this.usuario = usuario;
		this.dataAssociacao = dataAssociacao;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "tagBlueIdFk", column = @Column(name = "tag_blue_id_fk", nullable = false)),
			@AttributeOverride(name = "ocorrenciaIdFk", column = @Column(name = "ocorrencia_id_fk", nullable = false)) })
	public TagsBlueOcorrenciaId getId() {
		return this.id;
	}

	public void setId(TagsBlueOcorrenciaId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ocorrencia_id_fk", nullable = false, insertable = false, updatable = false)
	public Ocorrencia getOcorrencia() {
		return this.ocorrencia;
	}

	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_blue_id_fk", nullable = false, insertable = false, updatable = false)
	public TagBlue getTagBlue() {
		return this.tagBlue;
	}

	public void setTagBlue(TagBlue tagBlue) {
		this.tagBlue = tagBlue;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_associacao")
	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_associacao", length = 19)
	public Date getDataAssociacao() {
		return this.dataAssociacao;
	}

	public void setDataAssociacao(Date dataAssociacao) {
		this.dataAssociacao = dataAssociacao;
	}

}
