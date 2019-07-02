#作业1
## public void elementSwap(IntSet a) {}  
	/*@
      @ assignable a, this
      @ ensures (\forall int x; \old(a).isIn(x) ==> b.isIn(\old(a).isIn(x)));
      @ ensures (\forall int x; \old(this).isIn(x) ==> a.isIn(\old(this).isIn(x)));
      @ ensures (this.size() == \old(a).size() && a.size() == \old(this).size())
      @*/

## public IntSet symmetricDifference(IntSet a) throws NullPointerException {}  
	/*@ normal_behavior
      @ requires a.size() > 0 && this.size() > 0
      @ ensures (\exist IntSet c; (\forall int x; ((a.isIn(x) && !this.isIn(x))
      @ || (this.isIn(x) && !a.isIn(x))) ==> c.isIn(x)); \result == c);
      @ also
      @ exceptional_behavior
      @ signals (NullPointerException) a.size() <= 0 || this.size() <= 0;
      @*/

----------

#作业2

## equals(Object obj)
	该错误通过断言Assert.assertTrue(path1.equals(path2));得知，由于源代码直接用this == obj无法对不同类型的对象进行正确判断，因此改正为this.equals((MyPath) obj);

## addPath()
	该错误在通过id判断路径是否存在时报错的，路径添加是成功的，但添加处理有问题：maxId在构造方法时初始化为0，但是在addPath的时候先pathMap.put(path, this.maxId);pathIdMap.put(this.maxId, path);到map中，再maxId++,从而第一次add路径时this.maxId是0。应该改为先maxId++,再添加路径