package com.mgr.life.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mgr.life.RestEntity;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
@JsonDeserialize(as = Client.class)
public class Client extends RestEntity {

    private String name, surname, email, streetName, streetNumber, city, country;
    private Integer zipCode, phonePrefix;
    private Long phone;

    public Client() {
    }

    public Client(Long id, String name, String surname, String email, String streetName, String streetNumber,
                  String city, String country, Integer zipCode, Integer phonePrefix, Long phone) {
        super(id);
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.city = city;
        this.country = country;
        this.zipCode = zipCode;
        this.phonePrefix = phonePrefix;
        this.phone = phone;
    }

    public Client(String name, String surname, String email, String streetName, String streetNumber, String city,
                  String country, Integer zipCode, Integer phonePrefix, Long phone) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.city = city;
        this.country = country;
        this.zipCode = zipCode;
        this.phonePrefix = phonePrefix;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public Client setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public Client setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Client setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getStreetName() {
        return streetName;
    }

    public Client setStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public Client setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Client setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Client setCountry(String country) {
        this.country = country;
        return this;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public Client setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public Integer getPhonePrefix() {
        return phonePrefix;
    }

    public Client setPhonePrefix(Integer phonePrefix) {
        this.phonePrefix = phonePrefix;
        return this;
    }

    public Long getPhone() {
        return phone;
    }

    public Client setPhone(Long phone) {
        this.phone = phone;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(name, client.name) &&
                Objects.equals(surname, client.surname) &&
                Objects.equals(email, client.email) &&
                Objects.equals(streetName, client.streetName) &&
                Objects.equals(streetNumber, client.streetNumber) &&
                Objects.equals(city, client.city) &&
                Objects.equals(country, client.country) &&
                Objects.equals(zipCode, client.zipCode) &&
                Objects.equals(phonePrefix, client.phonePrefix) &&
                Objects.equals(phone, client.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, email, streetName, streetNumber, city, country, zipCode, phonePrefix, phone);
    }
}
