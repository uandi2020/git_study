package com.human.shop.dto;

public class loginDto {
	String m_id;
	String member_pw;
	String member_name;
	String member_date;
	String member_gene;
	String member_mail;
	String member_mobile;
	String member_adress;

	public loginDto() {	
	
	}

	public String getM_id() {
		return m_id;
	}

	public void setM_id(String m_id) {
		this.m_id = m_id;
	}

	public String getMember_pw() {
		return member_pw;
	}

	public void setMember_pw(String member_pw) {
		this.member_pw = member_pw;
	}

	public String getMember_date() {
		return member_date;
	}

	public void setMember_date(String member_date) {
		this.member_date = member_date;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

	public String getMember_gene() {
		return member_gene;
	}

	public void setMember_gene(String member_gene) {
		this.member_gene = member_gene;
	}

	public String getMember_mail() {
		return member_mail;
	}

	public void setMember_mail(String member_mail) {
		this.member_mail = member_mail;
	}

	public String getMember_mobile() {
		return member_mobile;
	}

	public void setMember_mobile(String member_mobile) {
		this.member_mobile = member_mobile;
	}

	public String getMember_adress() {
		return member_adress;
	}

	public void setMember_adress(String member_adress) {
		this.member_adress = member_adress;
	}

	public loginDto(String m_id, String member_pw, String member_date, String member_name, String member_gene,
			String member_mail, String member_mobile, String member_adress) {
//		super();
		this.m_id = m_id;
		this.member_pw = member_pw;
		this.member_date = member_date;
		this.member_name = member_name;
		this.member_gene = member_gene;
		this.member_mail = member_mail;
		this.member_mobile = member_mobile;
		this.member_adress = member_adress;
	}
}