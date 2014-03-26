package me.ghui.AMS.net;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.List;
        import java.util.Set;
        import org.apache.http.Header;
        import org.apache.http.HeaderElement;
        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.NameValuePair;
        import org.apache.http.ParseException;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.client.params.ClientPNames;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;
        import org.apache.http.util.EntityUtils;
        import org.jsoup.Jsoup;
        import org.jsoup.nodes.Document;
        import org.jsoup.nodes.Element;
        import org.jsoup.select.Elements;

public class Test {
    private static String LoginUrl = "http://222.200.98.171:81/login.aspx";
    private static String Host = "http://222.200.98.171:81";
    private static String mainUrl = "";
    private static String borrowedBooksUrl = "";
    private static String cookie = "";
    private static String location = "";

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        getMyBorrowedBooks();
    }

    public static void getMyBorrowedBooks() {
        try {
            Document document = Jsoup.parse(login());
            Elements elements1 = document
                    .getElementsContainingOwnText("当前借阅情况和续借");// 通过text关键字找到所要的<a>标签
            String url = elements1.first().attr("href");
            borrowedBooksUrl = mainUrl.substring(0,
                    mainUrl.lastIndexOf("/") + 1) + url;// 取值和mainUrl进行拼凑组织借阅情况地址
            getBookBorrowedData(getHtml(borrowedBooksUrl));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取借书情况具体数据（List<BookEntity>）
     *
     * @param src
     * @return List<BookEntity>
     */
    private static List<BookEntity> getBookBorrowedData(String src) {
        List<BookEntity> data = new ArrayList<BookEntity>();
        Document document = Jsoup.parse(src);
        Element element = document.select("[id=borrowedcontent]").first()
                .getElementsByTag("table").first();
        Elements elements2 = element.getElementsByTag("tr");
        for (Element temp2 : elements2) {
            Elements elements3 = temp2.getElementsByTag("td");
            BookEntity entity = new test().new BookEntity()
                    .setIsFullData(elements3.get(0).text())
                    .setData2Return(elements3.get(1).text())
                    .setName(elements3.get(2).text())
                    .setData2Borrowed(elements3.get(6).text());
            data.add(entity);

        }
        data.remove(0);
        System.out.println("借书情况\n");

        for (BookEntity temp : data) {
            System.out.println(temp.getName() + "\n" + temp.getData2Borrowed()
                    + "\n" + temp.getData2Return() + "\n"
                    + temp.getIsFullData());
        }
        return data;

    }

    /**
     * 图书馆登陆
     *
     * @param context
     * @return 返回登陆后的界面Html代码
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String login() throws ClientProtocolException, IOException {
        List<NameValuePair> parmasList = new ArrayList<NameValuePair>();
        parmasList = initLoginParmas("3110006527", "密码不告诉你");
        HttpPost post = new HttpPost(LoginUrl);
        post.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
        // 阻止自动重定向，目的是获取第一个ResponseHeader的Cookie和Location
        post.setHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=gbk");
        // 设置编码为GBK
        post.setEntity(new UrlEncodedFormEntity(parmasList, "GBK"));
        HttpResponse response = new DefaultHttpClient().execute(post);
        cookie = response.getFirstHeader("Set-Cookie").getValue();
        // 取得cookie并保存起来
        // System.out.println("cookie= " + cookie);
        location = response.getFirstHeader("Location").getValue();
        // 重定向地址，目的是连接到主页
        mainUrl = Host + location;
        // 构建主页地址
        String html = getHtml(mainUrl);
        return html;

    }

    /**
     * 获取网页HTML源代码
     *
     * @param url
     * @return
     * @throws ParseException
     * @throws IOException
     */

    private static String getHtml(String url) throws ParseException,
            IOException {
        // TODO Auto-generated method stub
        HttpGet get = new HttpGet(url);
        if ("" != cookie) {
            get.addHeader("Cookie", cookie);
        }
        HttpResponse httpResponse = new DefaultHttpClient().execute(get);
        HttpEntity entity = httpResponse.getEntity();
        return EntityUtils.toString(entity);
    }

    /**
     * 初始化参数
     *
     * @param userName
     * @param passWord
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static List<NameValuePair> initLoginParmas(String userName,
                                                      String passWord) throws ParseException, IOException {
        List<NameValuePair> parmasList = new ArrayList<NameValuePair>();
        HashMap<String, String> parmasMap = getLoginFormData(LoginUrl);
        Set<String> keySet = parmasMap.keySet();

        for (String temp : keySet) {
            if (temp.contains("Username")) {
                parmasMap.put(temp, userName);
            } else if (temp.contains("txtPas")) {
                parmasMap.put(temp, passWord);
            }
        }

        Set<String> keySet2 = parmasMap.keySet();
        System.out.println("表单内容：");
        for (String temp : keySet2) {
            System.out.println(temp + " = " + parmasMap.get(temp));
        }
        for (String temp : keySet2) {
            parmasList.add(new BasicNameValuePair(temp, parmasMap.get(temp)));
        }

        // System.out.println("initParams \n" + parmasMap);

        return parmasList;

    }

    /**
     * 获取登录表单input内容
     *
     * @param url
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static HashMap<String, String> getLoginFormData(String url)
            throws ParseException, IOException {
        Document document = Jsoup.parse(getHtml(url));
        Elements element1 = document.getElementsByTag("form");// 找出所有form表单
        Element element = element1.select("[method=post]").first();// 筛选出提交方法为post的表单
        Elements elements = element.select("input[name]");// 把表单中带有name属性的input标签取出
        HashMap<String, String> parmas = new HashMap<String, String>();
        for (Element temp : elements) {
            parmas.put(temp.attr("name"), temp.attr("value"));// 把所有取出的input，取出其name，放入Map中
        }
        return parmas;
    }

    class BookEntity {
        /**
         * 书名
         *
         */
        private String name;
        /**
         * 可借数
         */
        private String leandableNum;
        /**
         * 索引号
         */
        private String callNumber;
        /**
         * 作者
         */
        private String writer;
        /**
         * 出版社
         */
        private String publisher;
        /**
         * 还书时间
         */
        private String data2Return;
        /**
         * 借书时间
         */
        private String data2Borrowed;
        /**
         * 是否续满
         */
        private String isFullData;

        public BookEntity() {

        }

        public String getName() {
            return name;
        }

        public String getLeandableNum() {
            return leandableNum;
        }

        public String getCallNumber() {
            return callNumber;
        }

        public String getWriter() {
            return writer;
        }

        public String getPublisher() {
            return publisher;
        }

        public BookEntity setName(String name) {
            this.name = name;
            return this;
        }

        public BookEntity setLeandableNum(String leandableNum) {
            this.leandableNum = leandableNum;
            return this;
        }

        public BookEntity setCallNumber(String callNumber) {
            this.callNumber = callNumber;
            return this;
        }

        public BookEntity setWriter(String writer) {
            this.writer = writer;
            return this;
        }

        public BookEntity setPublisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public String getData2Return() {
            return data2Return;
        }

        public String getData2Borrowed() {
            return data2Borrowed;
        }

        public String getIsFullData() {
            return isFullData;
        }

        public BookEntity setData2Return(String data2Return) {
            this.data2Return = data2Return;
            return this;
        }

        public BookEntity setData2Borrowed(String data2Borrowed) {
            this.data2Borrowed = data2Borrowed;
            return this;
        }

        public BookEntity setIsFullData(String isFullData) {
            this.isFullData = isFullData;
            return this;
        }

    }

}
