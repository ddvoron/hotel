package com.voronovich.hotel.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.voronovich.hotel.pojo.Hotel;
import com.voronovich.hotel.pojo.HotelCategory;

/**
 * Service layer for HotelCategory entity
 * 
 * @author Dmitry V
 * @version 1.0
 */
public class HotelCategoryService {

	private static HotelCategoryService instance;
	private static final Logger LOGGER = Logger.getLogger(HotelCategoryService.class.getName());

	private final HashMap<Long, HotelCategory> hotelCategories = new HashMap<>();
	private long nextId = 0;

	/**
	 * Constructor without args
	 */
	private HotelCategoryService() {
	}

	/**
	 * Singleton for HotelService
	 * 
	 * @return
	 */
	public static HotelCategoryService getInstance() {
		if (instance == null) {
			instance = new HotelCategoryService();
			instance.ensureTestData();
		}
		return instance;
	}
	
	/**
	 * Returns Categories records
	 * 
	 * @return
	 */
	public synchronized List<HotelCategory> getAll() {
		return getAll(null);
	}

	public synchronized long count() {
		return hotelCategories.size();
	}

	/**
	 * Deletes one entity
	 * 
	 * @param value
	 */
	public synchronized void delete(HotelCategory value) {
		hotelCategories.remove(value.getId());
	}

	/**
	 * Deletes entity group
	 * 
	 * @param value
	 */
	public synchronized void deleteGroup(Set<HotelCategory> value) {
		for (HotelCategory hotelCategory : value) {
			hotelCategories.remove(hotelCategory.getId());
		}
	}

	/**
	 * Saves entity
	 * 
	 * @param entry
	 */
	public synchronized void save(HotelCategory entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "HotelCategory is null.");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = (HotelCategory) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		hotelCategories.put(entry.getId(), entry);
	}

	/**
	 * Returns records according to the filter string
	 * 
	 * @param stringFilter
	 * @return
	 */
	public synchronized List<HotelCategory> getAll(String stringFilter) {
		ArrayList<HotelCategory> arrayList = new ArrayList<>();
		for (HotelCategory hotel : hotelCategories.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| hotel.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(hotel.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(HotelService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<HotelCategory>() {

			@Override
			public int compare(HotelCategory o1, HotelCategory o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	/**
	 * Get data from massive for further work
	 * 
	 */
	public void ensureTestData() {
		if (getAll().isEmpty()) {
			final String[] hotelCategoriesData = new String[] { "Hotel", "Hostel", "GuestHouse", "Appartments" };

			for (String category : hotelCategoriesData) {
				HotelCategory hC = new HotelCategory();
				hC.setCategory(category);
				save(hC);
			}
		}
	}

}
