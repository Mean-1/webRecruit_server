package com.xxx.server.service;

import com.xxx.server.pojo.RespBean;
import com.xxx.server.pojo.ResumeSkill;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mean
 * @since 2023-04-05
 */
public interface IResumeSkillService extends IService<ResumeSkill> {

    /**
     * 根据resume_id获取Skill
     * @param resume_id
     * @return
     */
    RespBean getEducationByResumeId(Integer resume_id);

    /**
     * 删除skill
     * @param id
     * @return
     */
    RespBean deleteById(Integer id);
}
