package com.voronovich.hotel.pojo;

import java.io.Serializable;

/**
 * Pojo Hotel Category
 * 
 * @author Dmitry V
 * @version1.0
 */
public class HotelCategory implements Serializable, Cloneable {

	private static final long serialVersionUID = -2618480596003609196L;

	private Long id;
	private String category;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public HotelCategory() {

	}

	public HotelCategory(Long id, String category) {
		this.id = id;
		this.category = category;
	}

	public boolean isPersisted() {
		return id != null;
	}

	@Override
	public String toString() {
		return category;
	}

	@Override
	public HotelCategory clone() throws CloneNotSupportedException {
		return (HotelCategory) super.clone();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
