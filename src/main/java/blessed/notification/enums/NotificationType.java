package blessed.notification.enums;

public enum NotificationType {
    NON_CONFORMITY_CREATED {
        public String buildMessage(String reference){
            return "Uma nova não conformidade foi criada: " + reference;
        }
    },

    DISPOSITION_OWNER_ASSIGNED {
        public String buildMessage(String reference){
            return "Você foi definido como responsável da disposição no registro: " + reference;
        }
    },

    EFFECTIVENESS_ANALYST_ASSIGNED {
        public String buildMessage(String reference){
            return "Você foi definido como responsável pela análise de eficácia no registro: " + reference;
        }
    };

    public abstract String buildMessage(String reference);
}