package me.ghui.AMS.UI;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import me.ghui.AMS.R;
import me.ghui.AMS.domain.Teacher;
import me.ghui.AMS.net.NetUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by ghui on 4/7/14.
 */
public class SelfInfoActivity extends Activity {
    private Teacher teacher;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_info);
        init();
    }

    private void init() {
        layout = new RelativeLayout(this);
        ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setGravity(Gravity.CENTER);
        layout.addView(progressBar);
        this.addContentView(layout, params);
        getTeaInfo();
    }

    private void fillData(Teacher teacher) {
        if (teacher.getName().equals("")) {
            return;
        }
        ((TextView) findViewById(R.id.name)).setText(teacher.getName());
        ((TextView) findViewById(R.id.sex)).setText(teacher.getSex());
        ((TextView) findViewById(R.id.birthday)).setText(teacher.getBirthday());
        ((TextView) findViewById(R.id.nation)).setText(teacher.getNation());
        ((TextView) findViewById(R.id.id_card_no)).setText(teacher.getIDCardNo());
        ((TextView) findViewById(R.id.native_placle)).setText(teacher.getNativePlace());
        ((TextView) findViewById(R.id.degree)).setText(teacher.getDegree());
        ((TextView) findViewById(R.id.education)).setText(teacher.getEducation());
        ((TextView) findViewById(R.id.id)).setText(teacher.getId());
        ((TextView) findViewById(R.id.position)).setText(teacher.getPosition());
        ((TextView) findViewById(R.id.job_title)).setText(teacher.getJobTitle());
        ((TextView) findViewById(R.id.join_lit_time)).setText(teacher.getJoinLITTime());
        ((TextView) findViewById(R.id.work_status)).setText(teacher.getWorkStatus());
        ((TextView) findViewById(R.id.cell_phone_no)).setText(teacher.getCellPhoneNO());
        ((TextView) findViewById(R.id.phone)).setText(teacher.getTel());
        ((TextView) findViewById(R.id.email)).setText(teacher.getEmail());
        ((TextView) findViewById(R.id.resume)).setText(teacher.getResume());
    }

    private void getTeaInfo() {
        teacher = new Teacher();
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetUtils.getTeaInfo();
                Document document = Jsoup.parse(NetUtils.TEA_INFO_HTML);
                Elements elements = document.select("td[class]");
                Log.e("ghui", "size:" + elements.size());
                String content;
                for (int i = 0; i < elements.size(); i++) {
                    content =elements.get(i).text();
                    switch (i) {
                        case 0:
                            teacher.setId(content);
                            break;
                        case 1:
                            teacher.setName(content);
                            break;
                        case 2:
//                            teacher.setId(content);
                            break;
                        case 3:
                            teacher.setSex(content);
                            break;
                        case 4:
                            teacher.setBirthday(content);
                            break;
                        case 5:
                            teacher.setEducation(content);
                            break;
                        case 6:
                            teacher.setDegree(content);
                            break;
                        case 7:
                            teacher.setJobTitle(content);
                            break;
                        case 8:
                            teacher.setJoinLITTime(content);
                            break;
                        case 9:
                            teacher.setNation(content);
                            break;
                        case 10:
                            teacher.setIDCardNo(content);
                            break;
                        case 11:
                            teacher.setNativePlace(content);
                            break;
                        case 12:
                            teacher.setPosition(content);
                            break;
                        case 14:
                            teacher.setWorkStatus(content);
                            break;
                        case 15:
                            teacher.setTel(content);
                            break;
                        case 16:
                            teacher.setCellPhoneNO(content);
                            break;
                        case 17:
                            teacher.setEmail(content);
                            break;
                        case 18:
                            teacher.setResume(content);
                    }
                }
                Log.e("ghui", "Teacher: " + teacher);
                layout.post(new Runnable() {
                    @Override
                    public void run() {
                        layout.setVisibility(View.GONE);
                        fillData(teacher);
                    }
                });
            }
        }).start();
    }
}

/*
<td width="100" class="B">&nbsp;</td>           工号 0
<td width="100" class="T">李蒙&nbsp;</td>         姓名 1
<td width="100" colspan="2" rowspan="4" align="center" valign="top" class="BT" style="background-color:#F1F7FF"> 头像 2
<div align="center" border="0">
<img onerror="this.src=&quot;../_photo/blank.jpg&quot;" width="100" height="130" border="0" src="../_photo/Teacher/0000288AwcmK94uG9.JPG" />
</div></td>
<td class="T">男<br /></td>          性别 3
<td class="T" align="top" height="24"><br /></td> 出生日期 4
<td class="T" align="top">硕士研究生<br /></td>   学历 5
<td class="T" align="top">硕士学位<br /></td>    学位 6
<td class="T">副教授<br /></td>   职称 7
<td class="B"><br /></td>   入校年份 8
<td class="T" align="top">满族 <br /></td>  民族 9
<td class="T" align="left" colspan="3"><br /></td> 身份证号 10
<td class="B" align="top">江苏省<br /></td> 籍贯 11
<td class="B">教师<br /></td>   岗位  12
<td class="B" align="center" width="55">是否在岗</td> 13
<td class="B" align="center">是&nbsp;</td>  是否在岗  14
<td class="BT" align="top"><br /></td>  联系电话 15
<td class="BT" align="top" colspan="5"><br /></td> 手机号码 16
<td class="BT" align="top" colspan="5"> <br /></td> 电邮 17
//简历 18
<td class="BT" valign="middle" height="100" colspan="5"><textarea style="overflow-y:auto;height:110;width:99%;border:0px;background-color:#f4fffb;">2002年6月毕业于西安建筑科技大学计算机应用技术</textarea></td>
*/
