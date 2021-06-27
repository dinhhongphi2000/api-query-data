package org.apiquery.shared.data;

public class QueryFilterOption {
    private Boolean ignorecase = false;

    public QueryFilterOption() {
    }

    public Boolean isIgnoreCase() {
        return this.ignorecase;
    }

    public static QueryFilterOption createQueryFilterOption(String parameter) {
        if (parameter == null || parameter.isEmpty())
            return new QueryFilterOption();

        QueryFilterOption option = new QueryFilterOption();
        String[] parts = parameter.split("\\,");
        for (String p : parts) {
            //for single option, (if field exists, value = true, else default value is false)
            if (p.equals("ignoreCase")) {
                option.ignorecase = true;
                continue;
            }

            // //for compose option (key,value)
            // String[] keyValue = p.split("\\$");
            // if(keyValue.length != 2)
            //     continue;
            // if(keyValue[0].equals("afd"))
            //     option.afd = keyValue[1];
        }
        return option;
    }
}