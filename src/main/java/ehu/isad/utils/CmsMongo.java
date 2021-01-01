package ehu.isad.utils;

import java.util.Arrays;

public class CmsMongo {
    private String target;
    private Plugins plugins;

    public String getTarget() {
        return target;
    }

    public String getPlugins() {
        try{
            var emaitza=plugins.getMetaGenerator().toString();
            return emaitza;
        }
        catch (NullPointerException e){
            return "unknown";
        }
    }

    public Plugins getPlug(){
        return plugins;
    }


    @Override
    public String toString() {
        try {
            return "CmsMongo{" +
                    "target='" + target + '\'' +
                    ", plugins=" + plugins.getMetaGenerator() +
                    '}';
        } catch (NullPointerException e){
            return "{}";
        }
    }


    //Plugins class
    public class Plugins{
        private MetaGenerator MetaGenerator;
        private MongoCountry Country;

        public MetaGenerator getMetaGenerator() {
            return MetaGenerator;
        }

        public MongoCountry getCountry() {
            return Country;
        }


        //MetaGenerator class
        public class MetaGenerator{
            private String[] string;

            public String[] getString() {
                return string;
            }

            public void setString(String[] string) {
                this.string = string;
            }

            @Override
            public String toString() {
                return Arrays.toString(string);
            }
        }


        //MongoCountry class
        public class MongoCountry {
            private String[] module;
            private String[] string;

            public String getModule() {
                return module[0];
            }

            public String getString() {
                return string[0];
            }

            public void setString(String[] string) {
                this.string = string;
            }
        }
    }

}
