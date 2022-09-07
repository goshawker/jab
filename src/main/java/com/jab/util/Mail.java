package com.jab.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


// TODO: Auto-generated Javadoc
/**
 * @description: TODO
 * @author mike
 * @date 2022/9/7 14:18
 * @version 1.0
 */
public class Mail {

	/** The from addr. */
	private String fromAddr = null;

	/** The to addr. */
	private String toAddr = null;

	/** The cc. */
	private String cc = null;

	/** The bcc. */
	private String bcc = null;

	/** The title. */
	private String title = null;

	/** The content. */
	private String content = null;

	/** The attachments. */
	private Map attachments = new HashMap();

	/**
	 * Gets the bcc.
	 * 
	 * @return the bcc
	 */
	public String getBcc() {
		return this.bcc;
	}

	/**
	 * Sets the bcc.
	 * 
	 * @param bcc
	 *            the new bcc
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	/**
	 * Gets the cc.
	 * 
	 * @return the cc
	 */
	public String getCc() {
		return this.cc;
	}

	/**
	 * Sets the cc.
	 * 
	 * @param cc
	 *            the new cc
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}

	/**
	 * Gets the from addr.
	 * 
	 * @return the from addr
	 */
	public String getFromAddr() {
		return this.fromAddr;
	}

	/**
	 * Sets the from addr.
	 * 
	 * @param fromAddr
	 *            the new from addr
	 */
	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}

	/**
	 * Gets the to addr.
	 * 
	 * @return the to addr
	 */
	public String getToAddr() {
		return this.toAddr;
	}

	/**
	 * Sets the to addr.
	 * 
	 * @param toAddr
	 *            the new to addr
	 */
	public void setToAddr(String toAddr) {
		this.toAddr = toAddr;
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the content.
	 * 
	 * @return the content
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Sets the content.
	 * 
	 * @param content
	 *            the new content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Adds the attachment.
	 * 
	 * @param attachPath
	 *            the attach path
	 * @param attachName
	 *            the attach name
	 */
	public void addAttachment(String attachPath, String attachName) {
		this.attachments.put(attachName, attachPath);
	}

	/**
	 * Gets the attach names.
	 * 
	 * @return the attach names
	 */
	public Set getAttachNames() {
		return this.attachments.keySet();
	}

	/**
	 * Gets the attach path.
	 * 
	 * @param attachName
	 *            the attach name
	 * @return the attach path
	 */
	public String getAttachPath(String attachName) {
		return (String) this.attachments.get(attachName);
	}
}