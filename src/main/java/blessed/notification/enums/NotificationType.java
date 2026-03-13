package blessed.notification.enums;

public enum NotificationType {

    NON_CONFORMITY_CREATED {
        public String buildMessage(String reference){
            return "Uma nova não conformidade foi criada: " + reference;
        }
    },

    NON_CONFORMITY_APPROVED {
        public String buildMessage(String reference){
            return "A não conformidade '" + reference + "' foi aprovada e seguirá para a próxima etapa do tratamento.";
        }
    },

    NON_CONFORMITY_RETURNED_FOR_CORRECTION {
        public String buildMessage(String reference){
            return "A não conformidade '" + reference + "' foi enviada para correção. Verifique os ajustes solicitados.";
        }
    },

    NON_CONFORMITY_RESUBMITTED_FOR_APPROVAL {
        public String buildMessage(String reference){
            return "A não conformidade '" + reference + "' foi corrigida e reenviada para aprovação.";
        }
    },

    DISPOSITION_OWNER_ASSIGNED {
        public String buildMessage(String reference){
            return "Você foi definido como responsável pela disposição no registro: " + reference;
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

    QUALITY_TOOL_COMPLETED {
        public String buildMessage(String reference){
            return "A ferramenta de qualidade da não conformidade '" + reference + "' foi concluída.";
        }
    },

    ROOT_CAUSE_REQUIRED {
        public String buildMessage(String reference){
            return "A não conformidade '" + reference + "' foi aprovada. Insira a causa raiz para continuar o tratamento.";
        }
    },

    ROOT_CAUSE_COMPLETED {
        public String buildMessage(String reference){
            return "A causa raiz da não conformidade '" + reference + "' foi registrada.";
        }
    },

    ACTION_ASSIGNED {
        public String buildMessage(String reference){
            return "Uma nova ação foi atribuída a você: " + reference;
        }
    },

    ACTION_COMPLETED {
        public String buildMessage(String reference){
            return "Uma ação foi concluída no registro '" + reference + "'.";
        }
    },

    ACTION_NOT_COMPLETED {
        public String buildMessage(String reference){
            return "Uma ação foi marcada como não realizada no registro '" + reference + "'.";
        }
    },

    DISPOSITION_COMPLETED {
        public String buildMessage(String reference){
            return "A disposição da não conformidade '" + reference + "' foi concluída. O registro já está disponível para análise de eficácia.";
        }
    },

    EFFECTIVENESS_APPROVED {
        public String buildMessage(String reference){
            return "A análise de eficácia da não conformidade '" + reference + "' foi concluída como EFICAZ.";
        }
    },

    EFFECTIVENESS_REJECTED {
        public String buildMessage(String reference){
            return "A análise de eficácia da não conformidade '" + reference + "' foi concluída como NÃO EFICAZ.";
        }
    };

    public abstract String buildMessage(String reference);
}