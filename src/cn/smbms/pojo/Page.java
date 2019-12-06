package cn.smbms.pojo;

//用于分页的page实体类
public class Page {

	private Integer currPageNo; // 当前页码
	private Integer pageSize; // 每页显示的数据行数
	private Integer totalCount; // 总的记录数
	private Integer totalPageCount; // 总的页数

	public Integer getCurrPageNo() {
		return currPageNo;
	}

	public void setCurrPageNo(Integer currPageNo) {
		this.currPageNo = currPageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	// 要给总记录数赋值时，要计算总的页数
	public void setTotalCount(Integer totalCount) {
		if (totalCount > 0) {
			this.totalCount = totalCount;

			// 根据pageSize及totalCount计算总的页数。 如总的记录数20行，每页显示 6行，问总的页数
			this.totalPageCount = this.totalCount % this.pageSize == 0 ? this.totalCount
					/ this.pageSize
					: this.totalCount / this.pageSize + 1;
		}
	}

	public Integer getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(Integer totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
}