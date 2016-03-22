#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end
#parse("File Header.java")
public class ${NAME}{

    private static ${NAME} instance;

    private ${NAME}() {
    }
    
    public static ${NAME} instance() {
        return instance != null ? instance : (instance = new ${NAME}());
    }
}