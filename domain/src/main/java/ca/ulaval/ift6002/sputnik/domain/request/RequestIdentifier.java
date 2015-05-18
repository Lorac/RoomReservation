package ca.ulaval.ift6002.sputnik.domain.request;

import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class RequestIdentifier implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column
    private UUID number;

    protected RequestIdentifier() {
    }

    public RequestIdentifier(UUID number) {
        this.number = number;
    }

    public static RequestIdentifier create() {
        return new RequestIdentifier(UUID.randomUUID());
    }

    public String describe() {
        return number.toString();
    }

    public boolean isSame(RequestIdentifier identifier) {
        return EqualsBuilder.reflectionEquals(this, identifier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestIdentifier that = (RequestIdentifier) o;

        return isSame(that);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }
}
