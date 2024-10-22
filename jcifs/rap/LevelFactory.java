package jcifs.rap;

public class LevelFactory {

    public static Info createInformationLevel(Class base, int level) {
        try {
            if (level == 0) {
                try {
                    return (Info)
                            Class.forName(base.getName() + 0).newInstance();
                } catch (ClassNotFoundException ex) { }
                return (Info) base.newInstance();
            }
            return (Info) Class.forName(base.getName() + level).newInstance();
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Unsupported information level for " + base.getName() +
                            ": " + level);
        }
    }

}
