package blessed.notification.enums;

public enum NotificationType {
    NON_CONFORMITY_CREATED {
        public String buildMessage(String reference){
            return "Uma nova não conformidade foi criada: " + reference;
        }
    },

    NON_CONFORMITY_RETURNED_FOR_CORRECTION {
        public String buildMessage(String reference){
            return "A não conformidade '" + reference + "' foi enviada para correção. Verifique os ajustes solicitados.";
        }
    },

    FIVE_WHY_COMPLETED {
        public String buildMessage(String reference){
            return "A ferramenta de análise de causa (5 Porquês) da não conformidade '" + reference + "' foi concluída.";
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
    },

    QUALITY_TOOL_REQUIRED {
        public String buildMessage(String reference){
            return "A não conformidade '" + reference + "' foi aprovada. Preencha a ferramenta da qualidade para continuar a análise.";
        }
    },

    ROOT_CAUSE_REQUIRED {
        public String buildMessage(String reference){
            return "A não conformidade '" + reference + "' foi aprovada. Insira a causa raiz para continuar o tratamento.";
        }
    };

    public abstract String buildMessage(String reference);
}