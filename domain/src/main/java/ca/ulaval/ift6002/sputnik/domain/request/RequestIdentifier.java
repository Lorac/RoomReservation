package ca.ulaval.ift6002.sputnik.domain.request;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.UUID;

public class RequestIdentifier {

    private UUID number;

    public RequestIdentifier(UUID number) {
        this.number = number;
    }

    public static RequestIdentifier create() {
        return new RequestIdentifier(UUID.randomUUID());
    }

    public boolean isSame(RequestIdentifier identifier) {
        return EqualsBuilder.reflectionEquals(this, identifier);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }
}
