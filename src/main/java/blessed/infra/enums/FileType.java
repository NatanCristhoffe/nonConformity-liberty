package blessed.infra.enums;

import java.util.List;

public enum FileType {
    IMAGE(List.of("image/jpeg", "image/png", "image/webp")),
    EVIDENCE(List.of("image/jpeg", "image/png", "image/webp", "application/pdf"));


    private final List<String> mimeTypes;

    FileType(List<String> mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public boolean isAllowed(String mimeType) {
        return mimeTypes.contains(mimeType);
    }

    public List<String> getMimeTypes() {
        return mimeTypes;
    }

}
