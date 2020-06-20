package service;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import dao.IChoiceqstDAO;
import util.MybatisUtils;
import vo.Choiceqst;


public class ChoiceQstService {
	private IChoiceqstDAO choiceqstDAO = null;
	private SqlSession sqlSession = null;
	
	public ChoiceQstService() {
		this.sqlSession = MybatisUtils.getSqlSession();
		this.choiceqstDAO = sqlSession.getMapper(IChoiceqstDAO.class);
	}
	
	// 添加题目
		public Map<String, Object> addTopic(Choiceqst topic) {
			Map<String, Object> mapResult = new HashMap<String, Object>();
			try {
				// 判断该题号是否已经存在
				Choiceqst topicFound = this.choiceqstDAO.getById(topic.getTopic_id());
				if (topicFound == null) {
					int a = this.choiceqstDAO.insert(topic);
					if (a <= 0) {
						mapResult.put("code", 1);
						mapResult.put("msg", "添加失败");
					} else {
						mapResult.put("code", 2);
						mapResult.put("msg", "添加成功");
					}
				} else {
					mapResult.put("code", 1);
					mapResult.put("msg", "添加失败,因为该题号已经存在");
				}
			} catch (Exception e) {
				mapResult.put("code", 0);
				mapResult.put("msg", "添加过程中出现错误");
			} finally {
				this.sqlSession.close();
			}
			return mapResult;
		}

		// 获取题目
		public Choiceqst getTopicById(String topic_id) {
			Choiceqst topic = null;
			try {
				topic = this.choiceqstDAO.getById(topic_id);
			} catch (Exception e) {
				topic=null;
			} finally {
				this.sqlSession.close();
			}
			return topic;
		}

		// 更新题目
		public Map<String, Object> updateTopic(Choiceqst topic) {
			Map<String, Object> mapResult = new HashMap<String, Object>();
			try {
				// 判断该题号是否已经存在
				System.out.println(topic.getTopic_id());
				Choiceqst topicFound = this.choiceqstDAO.getById(topic.getTopic_id());
				if (topicFound != null) {
					int a = this.choiceqstDAO.update(topic);
					if (a < 0) {
						mapResult.put("code", 1);
						mapResult.put("msg", "更新失败");
					} else {
						mapResult.put("code", 2);
						mapResult.put("msg", "更新成功");
					}
				} else {
					mapResult.put("code", 1);
					mapResult.put("msg", "更新失败,因为该题号不存在！");
				}
			} catch (Exception e) {
				mapResult.put("code", 0);
				mapResult.put("msg", "更新过程中出现错误");
			} finally {
				this.sqlSession.close();
			}
			return mapResult;
		}

		// 删除题目
		public Map<String, Object> deleteTopic(String topic_id) {
			Map<String, Object> mapResult = new HashMap<String, Object>();
			try {
				int a = this.choiceqstDAO.delete(topic_id);
				if (a <= 0) {
					mapResult.put("code", 1);
					mapResult.put("msg", "不存在该题号");
				} else {
					mapResult.put("code", 2);
					mapResult.put("msg", "删除成功");
				}
			} catch (Exception e) {
				mapResult.put("code", 0);
				mapResult.put("msg", "删除过程中出现错误");
			} finally {
				this.sqlSession.close();
			}
			return mapResult;

		}
	
}
