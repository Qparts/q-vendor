package q.rest.vendor.model.entity.driver;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="vnd_driver_cities")
@IdClass(DriverCity.DriverCityPK.class)
public class DriverCity implements Serializable {

    @Id
    @Column(name="driver_id")
    private int driverId;
    @Id
    @Column(name="city_id")
    private int cityId;
    @Column(name="country_id")
    private int countryId;
    @Column(name="region_id")
    private int regionId;

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public static class DriverCityPK implements Serializable {
        private int driverId;
        private int cityId;

        public DriverCityPK() {
        }

        public int getDriverId() {
            return driverId;
        }

        public void setDriverId(int driverId) {
            this.driverId = driverId;
        }

        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DriverCityPK that = (DriverCityPK) o;
            return driverId == that.driverId &&
                    cityId == that.cityId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(driverId, cityId);
        }
    }
}
