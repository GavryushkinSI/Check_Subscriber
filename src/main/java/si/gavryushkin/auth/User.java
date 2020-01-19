package si.gavryushkin.auth;

public class User {
    private String FIO;
    private String phone;
    private String email;
    private String uuid;
    private String tariff;
    private String comment;
    private String blocked;
    private String blocked_cause;

    public User(String FIO, String phone, String email, String uuid, String tariff, String comment, String blocked, String blocked_cause) {
        this.FIO = FIO;
        this.phone = phone;
        this.email = email;
        this.uuid = uuid;
        this.tariff = tariff;
        this.comment = comment;
        this.blocked = blocked;
        this.blocked_cause = blocked_cause;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    public String getBlocked_cause() {
        return blocked_cause;
    }

    public void setBlocked_cause(String blocked_cause) {
        this.blocked_cause = blocked_cause;
    }

    @Override
    public String toString() {
        return "User{" +
                "FIO='" + FIO + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", uuid='" + uuid + '\'' +
                ", tariff='" + tariff + '\'' +
                ", comment='" + comment + '\'' +
                ", blocked='" + blocked + '\'' +
                ", blocked_cause='" + blocked_cause + '\'' +
                '}';
    }
}
