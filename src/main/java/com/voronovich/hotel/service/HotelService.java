package com.voronovich.hotel.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.voronovich.hotel.pojo.Hotel;
import com.voronovich.hotel.pojo.HotelCategory;

/**
 * Service layer for Hotel entity
 * 
 * @author Dmitry V
 * @version 1.0
 */
public class HotelService {

	private static HotelService instance;
	private static final Logger LOGGER = Logger.getLogger(HotelService.class.getName());
	private HotelCategoryService service = HotelCategoryService.getInstance();

	private final HashMap<Long, Hotel> hotels = new HashMap<>();
	private long nextId = 0;

	private HotelService() {
	}

	/**
	 * Singleton for Hotel
	 * 
	 * @return
	 */
	public static HotelService getInstance() {
		if (instance == null) {
			instance = new HotelService();
			instance.ensureTestData();
		}
		return instance;
	}

	/**
	 * Returns Hotels records
	 * 
	 * @return
	 */
	public synchronized List<Hotel> findAll() {
		return findAll(null);
	}

	/**
	 * Returns records according to the filter string
	 * 
	 * @param stringFilter
	 * @return
	 */
	public synchronized List<Hotel> findAll(String stringFilter) {
		ArrayList<Hotel> arrayList = new ArrayList<>();
		for (Hotel hotel : hotels.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| check(hotel.toString(),stringFilter);
				if (passesFilter) {
					arrayList.add(hotel.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(HotelService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Hotel>() {

			@Override
			public int compare(Hotel o1, Hotel o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	/**
	 * Additional method for checking conditions
	 * 
	 * @param field
	 * @param filter
	 * @return
	 */
	public boolean check(String field, String filter) {
		boolean result = field.toLowerCase().contains(filter.toLowerCase());
		return result;
	}

	/**
	 * Returns records according to the filter string
	 * 
	 * @param stringFilter
	 * @return
	 */
	public synchronized List<Hotel> findAllByNameAndAddress(String stringFilter, String stringFilter1) {
		ArrayList<Hotel> arrayList = new ArrayList<>();
		for (Hotel hotel : hotels.values()) {
			try {
				boolean passesFilter = ((stringFilter == null && stringFilter1 == null)
						|| (stringFilter.isEmpty() && stringFilter1.isEmpty()))
						|| (check(hotel.getName(), stringFilter) && check(hotel.getAddress(), stringFilter1))
						|| (stringFilter == null && check(hotel.getAddress(), stringFilter1))
						|| (check(hotel.getName(), stringFilter) && stringFilter1 == null);
				if (passesFilter) {
					arrayList.add(hotel.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(HotelService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Hotel>() {

			@Override
			public int compare(Hotel o1, Hotel o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	public synchronized long count() {
		return hotels.size();
	}

	/**
	 * Deletes entity group
	 * 
	 * @param value
	 */
	public synchronized void delete(Hotel value) {
		hotels.remove(value.getId());
	}
	
	/**
	 * Saves entity
	 * 
	 * @param entry
	 */
	public synchronized void save(Hotel entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "Hotel is null.");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = (Hotel) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		hotels.put(entry.getId(), entry);
	}

	/**
	 * Get data from massive for further work
	 * 
	 */
	public void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] hotelData = new String[] {
					"3 Nagas Luang Prabang - MGallery by Sofitel;4;https://www.booking.com/hotel/la/3-nagas-luang-prabang-by-accor.en-gb.html;Vat Nong Village, Sakkaline Road, Democratic Republic Lao, 06000 Luang Prabang, Laos;",
					"Abby Boutique Guesthouse;1;https://www.booking.com/hotel/la/abby-boutique-guesthouse.en-gb.html;Ban Sawang , 01000 Vang Vieng, Laos",
					"Bountheung Guesthouse;1;https://www.booking.com/hotel/la/bountheung-guesthouse.en-gb.html;Ban Tha Heua, 01000 Vang Vieng, Laos",
					"Chalouvanh Hotel;2;https://www.booking.com/hotel/la/chalouvanh.en-gb.html;13 road, Ban Phonesavanh, Pakse District, 01000 Pakse, Laos",
					"Chaluenxay Villa;3;https://www.booking.com/hotel/la/chaluenxay-villa.en-gb.html;Sakkarin Road Ban Xienthong Luang Prabang Laos, 06000 Luang Prabang, Laos",
					"Dream Home Hostel 1;1;https://www.booking.com/hotel/la/getaway-backpackers-hostel.en-gb.html;049 Sihome Road, Ban Sihome, 01000 Vientiane, Laos",
					"Inpeng Hotel and Resort;2;https://www.booking.com/hotel/la/inpeng-and-resort.en-gb.html;406 T4 Road, Donekoy Village, Sisattanak District, 01000 Vientiane, Laos",
					"Jammee Guesthouse II;2;https://www.booking.com/hotel/la/jammee-guesthouse-vang-vieng1.en-gb.html;Vang Vieng, 01000 Vang Vieng, Laos",
					"Khemngum Guesthouse 3;2;https://www.booking.com/hotel/la/khemngum-guesthouse-3.en-gb.html;Ban Thalat No.10 Road Namngum Laos, 01000 Thalat, Laos",
					"Khongview Guesthouse;1;https://www.booking.com/hotel/la/khongview-guesthouse.en-gb.html;Ban Klang Khong, Khong District, 01000 Muang Không, Laos",
					"Kong Kham Pheng Guesthouse;1;https://www.booking.com/hotel/la/kong-kham-pheng-guesthouse.en-gb.html;Mixay Village, Paksan district, Bolikhamxay province, 01000 Muang Pakxan, Laos",
					"Laos Haven Hotel & Spa;3;https://www.booking.com/hotel/la/laos-haven.en-gb.html;047 Ban Viengkeo, Vang Vieng , 01000 Vang Vieng, Laos",
					"Lerdkeo Sunset Guesthouse;1;https://www.booking.com/hotel/la/lerdkeo-sunset-guesthouse.en-gb.html;Muang Ngoi Neua,Ban Ngoy-Nua, 01000 Muang Ngoy, Laos",
					"Luangprabang River Lodge Boutique 1;3;https://www.booking.com/hotel/la/luangprabang-river-lodge.en-gb.html;Mekong River Road, 06000 Luang Prabang, Laos",
					"Manichan Guesthouse;2;https://www.booking.com/hotel/la/manichan-guesthouse.en-gb.html;Ban Pakham Unit 4/143, 60000 Luang Prabang, Laos",
					"Mixok Inn;2;https://www.booking.com/hotel/la/mixok-inn.en-gb.html;188 Sethathirate Road , Mixay Village , Chanthabuly District, 01000 Vientiane, Laos",
					"Ssen Mekong;2;https://www.booking.com/hotel/la/muang-lao-mekong-river-side-villa.en-gb.html;Riverfront, Mekong River Road, 06000 Luang Prabang, Laos",
					"Nammavong Guesthouse;2;https://www.booking.com/hotel/la/nammavong-guesthouse.en-gb.html;Ban phone houang Sisalearmsak Road , 06000 Luang Prabang, Laos",
					"Niny Backpacker hotel;1;https://www.booking.com/hotel/la/niny-backpacker.en-gb.html;Next to Wat Mixay, Norkeokhunmane Road., 01000 Vientiane, Laos",
					"Niraxay Apartment;2;https://www.booking.com/hotel/la/niraxay-apartment.en-gb.html;Samsenthai Road Ban Sihom , 01000 Vientiane, Laos",
					"Pakse Mekong Hotel;2;https://www.booking.com/hotel/la/pakse-mekong.en-gb.html;No 062 Khemkong Road, Pakse District, Champasak, Laos, 01000 Pakse, Laos",
					"Phakchai Hotel;2;https://www.booking.com/hotel/la/phakchai.en-gb.html;137 Ban Wattay Mueng Sikothabong Vientiane Laos, 01000 Vientiane, Laos",
					"Phetmeuangsam Hotel;2;https://www.booking.com/hotel/la/phetmisay.en-gb.html;Ban Phanhxai, Xumnuea, Xam Nua, 01000 Xam Nua, Laos" };

			Random r = new Random(0);
			for (String hotel : hotelData) {
				String[] split = hotel.split(";");
				Hotel h = new Hotel();
				h.setName(split[0]);
				h.setRating(Integer.parseInt(split[1]));
				h.setUrl(split[2]);
				h.setAddress(split[3]);
				h.setCategory((service.getAll().get(r.nextInt(service.getAll().size()))).getCategory());
				long daysOld = r.nextInt(365 * 30);
				h.setOperatesFrom(daysOld);
				save(h);
			}
		}
	}
}
