public class EditorContentStore {
    private static String content = "Teste";
    public static String getCurrentContent(){
        return content;
    }

    public static void updateContent(String content){
        EditorContentStore.content = content;
    }
}
