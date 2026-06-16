# 期末复习补充速查：SQL 实战 + Linux 高频

根据已创建的复习框架，SQL 约 40% 分值，Linux 常考脚本与权限。
重点补「常见考题写法」和「易混淆点辨析」，不重复框架已有内容。

---

## 一、SQL 实战速查（高频题型）

### 1. 建表与约束（DDL 常考）

```sql
-- 完整建表（含约束）
CREATE TABLE Student (
    Sno CHAR(9) PRIMARY KEY,                    -- 实体完整性
    Sname VARCHAR(20) NOT NULL,
    Ssex CHAR(2) CHECK(Ssex IN ('男','女')),   -- 用户定义完整性
    Sage SMALLINT,
    Sdept VARCHAR(20)
);

CREATE TABLE SC (
    Sno CHAR(9),
    Cno CHAR(4),
    Grade SMALLINT,
    PRIMARY KEY(Sno, Cno),                      -- 联合主键
    FOREIGN KEY(Sno) REFERENCES Student(Sno),   -- 参照完整性
    FOREIGN KEY(Cno) REFERENCES Course(Cno)
);

-- 表结构修改
ALTER TABLE Student ADD birthday DATE;
ALTER TABLE Student DROP COLUMN birthday;
ALTER TABLE Student ALTER COLUMN Sname VARCHAR(30);  -- SQL Server
```

### 2. 单表查询 —— 必须熟练

```sql
-- WHERE + 比较
SELECT Sname, Sage FROM Student WHERE Sage < 20;

-- 模糊查询
SELECT * FROM Student WHERE Sname LIKE '张%';   -- 姓张
SELECT * FROM Student WHERE Sname LIKE '_强%';  -- 第二个字是"强"
SELECT * FROM Student WHERE Sname LIKE '%明%';  -- 名字包含"明"

-- 去重
SELECT DISTINCT Sdept FROM Student;

-- 聚合 + GROUP BY + HAVING（必考组合）
SELECT Sdept, COUNT(*) AS 人数, AVG(Sage) AS 平均年龄
FROM Student
GROUP BY Sdept
HAVING COUNT(*) > 3;

-- ⚠️ WHERE 先过滤行，GROUP BY 分组，HAVING 后过滤组
-- HAVING 后面只能跟聚合函数或 GROUP BY 中的列

-- ORDER BY
SELECT Sno, Grade FROM SC WHERE Cno='C001'
ORDER BY Grade DESC;    -- 降序

-- 排序后取前 N 条
SELECT TOP 3 * FROM SC ORDER BY Grade DESC;       -- SQL Server
SELECT * FROM SC ORDER BY Grade DESC LIMIT 3;     -- MySQL/PostgreSQL
```

### 3. 多表连接 —— 高频题

```sql
-- 等值连接（查选了课的学生及其成绩）
SELECT Student.Sno, Sname, Cno, Grade
FROM Student, SC
WHERE Student.Sno = SC.Sno;

-- INNER JOIN（推荐写法，更清晰）
SELECT S.Sno, Sname, Cno, Grade
FROM Student S
INNER JOIN SC ON S.Sno = SC.Sno;

-- LEFT JOIN（查所有学生及其选课，没选课的显示 NULL）
SELECT S.Sno, Sname, Cno, Grade
FROM Student S
LEFT JOIN SC ON S.Sno = SC.Sno;

-- 三表连接（学生→选课→课程）
SELECT S.Sno, Sname, Cname, Grade
FROM Student S
JOIN SC ON S.Sno = SC.Sno
JOIN Course C ON SC.Cno = C.Cno;
```

### 4. 嵌套查询 —— 区分 IN / EXISTS

```sql
-- IN：子查询返回一列值（适合小结果集）
SELECT Sname FROM Student
WHERE Sno IN (
    SELECT Sno FROM SC WHERE Cno = 'C001'
);

-- EXISTS：相关子查询，逐行判断（适合大表，性能好）
SELECT Sname FROM Student S
WHERE EXISTS (
    SELECT 1 FROM SC WHERE Sno = S.Sno AND Cno = 'C001'
);

-- 查"没选某课"的学生（NOT EXISTS）
SELECT Sname FROM Student S
WHERE NOT EXISTS (
    SELECT 1 FROM SC WHERE Sno = S.Sno AND Cno = 'C001'
);

-- ⭐ 带量词的查询（ALL / ANY）
-- 查比"CS"系所有学生年龄都大的学生
SELECT Sname, Sage FROM Student
WHERE Sage > ALL (
    SELECT Sage FROM Student WHERE Sdept = 'CS'
);
```

### 5. 集合运算 —— 并/交/差

```sql
-- 查选了 C001 或 C002 的学生（并）
SELECT Sno FROM SC WHERE Cno = 'C001'
UNION
SELECT Sno FROM SC WHERE Cno = 'C002';

-- 两个系的学生（并）
SELECT * FROM Student WHERE Sdept = 'CS'
UNION
SELECT * FROM Student WHERE Sdept = 'MA';
-- UNION 自动去重，UNION ALL 保留重复
```

### 6. 数据更新（DML 常考）

```sql
-- INSERT（全列省略列名 / 指定列）
INSERT INTO Student VALUES ('2024001', '张三', '男', 20, 'CS');
INSERT INTO Student(Sno, Sname, Sdept) VALUES ('2024002', '李四', 'MA');

-- 子查询插入（从旧表筛选插入新表）
INSERT INTO CS_Student(Sno, Sname)
SELECT Sno, Sname FROM Student WHERE Sdept = 'CS';

-- UPDATE（常忘 WHERE！不加就是改全表）
UPDATE Student SET Sage = Sage + 1 WHERE Sdept = 'CS';

-- DELETE（同样要注意 WHERE）
DELETE FROM SC WHERE Grade < 60;
```

---

## 二、范式判断 —— 计算题必会

| 范式 | 条件 | 判断口诀 |
|------|------|----------|
| 1NF | 属性不可再分 | 表里没有"表" |
| 2NF | 1NF + 非主属性完全依赖于候选码 | 没有**部分**依赖 |
| 3NF | 2NF + 非主属性不传递依赖于候选码 | 没有**传递**依赖 |
| BCNF | 所有决定因素都包含候选码 | 左边全是码 |

**判断流程**：
1. 找出候选码（主键候选）
2. 列出所有函数依赖 A→B
3. 检查是否有非主属性对候选码的**部分依赖** → 是否满足 2NF
4. 检查是否满足 3NF / BCNF

**例题**：
关系 R(A, B, C, D)，函数依赖 F = {AB→C, C→D, D→A}
- 候选码：AB（由 AB→C 推 ABC，由 C→D 推 D，由 D→A 已有）—— 实际 AB 能推出全部
- 还有 BD、BC 等也是候选码
- 所有非主属性（没有非主属性，全是主属性）→ 至少 3NF
- 检查 BCNF：C→D 的决定因素 C 不是候选码 → **不满足 BCNF**

---

## 三、Linux 高频精简卡

### 权限计算速查

```
r = 4, w = 2, x = 1

  rwx  r-x  r--
  421  4-1  4--
  └─7──┘─5──┘─4── = 754
  所有  组  其他

chmod 755 file      # -rwxr-xr-x
chmod u+x file      # 所有者加执行
chmod go-w file     # 组和其他去掉写
```

### Shell 脚本常见写法

```bash
#!/bin/bash
# 位置参数
echo "脚本名: $0"
echo "参数一: $1"
echo "参数个数: $#"

# if 条件
if [ $# -eq 0 ]; then
    echo "请提供参数"
    exit 1
fi

# for 循环
for file in *.txt; do
    echo "处理: $file"
done

# while 读文件
while read line; do
    echo $line
done < /etc/passwd

# case 分支
case $1 in
    start) echo "启动..." ;;
    stop)  echo "停止..." ;;
    *)     echo "用法: $0 {start|stop}" ;;
esac
```

### 重定向辨析

| 写法 | 含义 |
|------|------|
| `cmd > file` | stdout 覆盖写入 |
| `cmd >> file` | stdout 追加写入 |
| `cmd 2> file` | stderr 覆盖写入 |
| `cmd &> file` | stdout+stderr 都写入 |
| `cmd < file` | 从文件读取输入 |
| `cmd1 \| cmd2` | cmd1 的输出传给 cmd2 |

---

## 四、Web 易混淆点辨析

### CSS 选择器优先级（背下来）
**`!important` > 内联 > ID (#) > 类(.) / 属性[] / 伪类(:) > 元素 / 伪元素(::)**

数值化记忆（100分制）：
- 内联样式：1000
- ID 选择器：0100
- 类/属性/伪类：0010
- 元素/伪元素：0001
- `!important`：无视一切

### 盒模型差异
```
标准盒模型：width = content（不含 padding/border）
IE 怪异盒模型：width = content + padding + border

box-sizing: content-box;   → 标准
box-sizing: border-box;    → 怪异（常用，更直观）
```

### JS 变量区别
| 关键字 | 作用域 | 可重复声明 | 变量提升 |
|--------|--------|-----------|---------|
| var | 函数级 | ✅ | ✅（undefined）|
| let | 块级 | ❌ | ❌（TDZ）|
| const | 块级 | ❌ | ❌（TDZ，必须初始化）|

---

整理时间：2026-06-10
若需要某科的真题预测、更多例题、或补充题型，随时说。
