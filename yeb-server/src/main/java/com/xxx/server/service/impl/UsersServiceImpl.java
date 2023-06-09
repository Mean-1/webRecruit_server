package com.xxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxx.server.config.security.component.JwtTokenUtil;
import com.xxx.server.mapper.ResumeMapper;
import com.xxx.server.mapper.UserResumeMapper;
import com.xxx.server.mapper.UsersMapper;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.pojo.Resume;
import com.xxx.server.pojo.UserResume;
import com.xxx.server.pojo.Users;
import com.xxx.server.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mean
 * @since 2023-03-06
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {


    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private UserResumeMapper userResumeMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;



    /**
     * 登录判断
     * @param phone
     * @param password
     * @param code
     * @param request
     * @return
     */
    @Override
    public RespBean login(String phone, String password, String code, HttpServletRequest request) {

        //通过phone获取username
        String username = usersMapper.selectOne(new QueryWrapper<Users>().eq("phone", phone).eq("enabled", true)).getUsername();

        if(null==username) return RespBean.error("该用户不存在");

        //登录判断
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);//UserDetailsService接口实现类是UserDetails,都是在Spring security 依赖下
        if(null==userDetails){
            return RespBean.error("用户名错误");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){

            return RespBean.error("密码错误");
        }
        if(!userDetails.isEnabled()){
            return RespBean.error("账号已被禁用，请联系管理员");
        }

        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken= new
                UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String,String > tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return RespBean.success("success",tokenMap);
    }

    /**
     * 根据username获取user对象信息
     * @param username
     * @return
     */
    @Override
    public Users getUserByUsername(String username) {
        return usersMapper.selectOne(new QueryWrapper<Users>().eq("username",username).eq("enabled",true));
    }

    /**
     * 注册
     * @param phone
     * @param password
     * @param code
     * @param request
     * @return
     */
    @Override
    public RespBean register(String phone, String password, String code, HttpServletRequest request) {


        //随机用户名
        String str = "用户"+ ((int) (Math.random() * 1000));
        if(null==phone||null==password){
            return RespBean.error("账号或密码为空，添加失败");
        }


        //插入数据
        int uid = usersMapper.addUser(str,phone, passwordEncoder.encode(password),code);
//        System.out.println("uid:"+uid);
        if(0!=uid){
            //注册用户后添加用户对应的一个空简历
//            int rid= resumeMapper.insertOneDate();
//            System.out.println("rid:"+rid);
//            UserResume userResume = new UserResume();
//            userResume.setResume_id(rid);
//            userResume.setUser_id(uid);
//            if(0!=userResumeMapper.insert(userResume)){
//                return RespBean.success("success");
//            }else {
//                return RespBean.error("注册用户后添加简历失败");
//            }
            return RespBean.success("success");
        }
        else
            return RespBean.error("添加失败");
    }

    /**
     * 更新用户的phone
     * @param id
     * @param phone
     * @return
     */
    @Override
    public RespBean updatePhone(Integer id, String phone) {
        if(null!=id&&null!=phone){

            if(1==usersMapper.updatePhone(id,phone)){
                return RespBean.success("success");
            }

        }
        return RespBean.error("更新失败");
    }

    /**
     * 更新用户的密码
     * @param id
     * @param newPassword
     * @return
     */
    @Override
    public RespBean updatePassword(Integer id, String newPassword) {
        if(null!=id&&null!=newPassword){
            if(1==usersMapper.updatePassword(id,passwordEncoder.encode(newPassword))){
                return RespBean.success("success");
            }
        }
        return RespBean.error("更新失败");
    }

    /**
     * 更新用户的username
     * @param id
     * @param newUsername
     * @return
     */
    @Override
    public RespBean updateUsername(Integer id, String newUsername) {

        if(null!=id&&null!=newUsername){

            if(0!=usersMapper.updateUsername(id,newUsername)){
                return RespBean.success("success");
            }
        }
        return RespBean.error("更新username失败");
    }

    /**
     * 更新用户的email
     * @param id
     * @param newEmail
     * @return
     */
    @Override
    public RespBean updateEmail(Integer id, String newEmail) {

        if(null!=id&&null!=newEmail){

            if(0!=usersMapper.updateEmail(id,newEmail)){
                return RespBean.success("success");
            }
        }
        return RespBean.error("更新email失败");
    }

    /**
     * 根据uid获取用户信息
     * @param id
     * @return
     */
    @Override
    public RespBean getUserInfoByuid(Integer id) {
        Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("id", id));
        if(null!=users){
            return RespBean.success("success",users);
        }
        return RespBean.error("error");
    }


}
