package com.jingwei;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页通用类
 * 
 * @author xianwen.tan
 *
 * @param <T>
 */
public class Page<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8102012822572335930L;
	protected PageInfo pageInfo;
	protected long totalCount;
	protected List<T> content;

	public Page(PageInfo pageInfo) {
		this(pageInfo, 0, null);
	}

	public Page(PageInfo pageInfo, long totalCount, List<T> content) {
		if (null == pageInfo) {
			pageInfo = new PageInfo();
		}
		this.pageInfo = pageInfo;
		this.totalCount = totalCount;
	}

	/**
	 * 获取总页数
	 * 
	 * @return
	 */
	public int getTotalPages() {
		return (int) Math.ceil((double) totalCount
				/ (double) pageInfo.getPageSize());
	}

	/**
	 * 获取总记录数
	 * 
	 * @return
	 */
	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 获取当前页号
	 * 
	 * @return
	 */
	public int getPageNo() {
		return pageInfo.getPageNo();
	}

	public int getPageSize() {
		return pageInfo.getPageSize();
	}

	/**
	 * 获取分页内容
	 * 
	 * @return
	 */
	public List<T> getContent() {
		if (null == content) {
			return new ArrayList<T>(0);
		}

		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	/**
	 * 判断是否有内容
	 * 
	 * @return
	 */
	public boolean hasContent() {
		return null != content && !content.isEmpty();
	}

	/**
	 * 获取排序参数
	 * 
	 * @return
	 */
	public String getSort() {
		return pageInfo.getSort();
	}

	public boolean isFirst() {
		return !pageInfo.hasPrevious();

	}

	public boolean isLast() {
		return !hasNext();
	}

	public boolean hasNext() {
		return getPageNo() + 1 < getTotalPages();
	}

	public boolean hasPrevious() {
		return pageInfo.hasPrevious();
	}

	public PageInfo nextPageInfo() {
		return hasNext() ? pageInfo.next() : null;
	}

	public PageInfo previousPageInfo() {
		return hasPrevious() ? pageInfo.previous() : null;
	}
}
