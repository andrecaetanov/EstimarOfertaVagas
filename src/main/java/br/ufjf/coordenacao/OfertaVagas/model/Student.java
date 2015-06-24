package br.ufjf.coordenacao.OfertaVagas.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Student implements Comparable<Student>{

	private HashMap<ClassStatus, HashMap<Class, ArrayList<String[]>>> classes = new HashMap<ClassStatus, HashMap<Class, ArrayList<String[]>>>();
	private String _id;
	private String _nome;
	private String _curriculum;
	private int _firstSemester;
	
	public String getNome() {
		return _nome;
	}

	public void setNome(String _nome) {
		this._nome = _nome;
	}

	public String getCurriculum() {
		return _curriculum;
	}

	public void setCurriculum(String curriculum) {
		this._curriculum = curriculum;
	}
	
	public Student(String id) {
		this._id = id;
		this._firstSemester = 0;
	}
	
	public Student(String id, String nome) {
		this._id = id;
		this._nome = nome;
		this._firstSemester = 0;
	}
	
	public float getIRA() { return IRA(0, 99999); }
	public float getIRA(int semester) { return IRA(this._firstSemester, semester); }
	public float getSemesterIRA(int semester) { return IRA(semester, semester); }
	
	private float IRA(int firstSemester, int lastSemester)
	{
		HashMap<Class, ArrayList<String[]>> cl;
		int grade = 0;
		int weight = 0;

		cl = this.classes.get(ClassStatus.APPROVED);
		if(cl != null) for(Class c: cl.keySet())
		{
			ArrayList<String[]> classdata = cl.get(c);
			for(String[] s2: classdata)
			{
				//TODO verificar excecoes do calculo - #3 (A a E)
				if(s2[1].equals("APR") || s2[1].equals("DISP") || s2[1].equals("A"))
					continue;

				//Verifica se o semestre esta dentro do intervalo definido
				if(Integer.valueOf(s2[0]) < firstSemester || Integer.valueOf(s2[0]) > lastSemester)
					continue;

				grade += Integer.valueOf(s2[1]) * c.getWeight();
				weight += c.getWeight();
			}
		}

		cl = this.classes.get(ClassStatus.REPROVED_GRADE);
		if(cl != null) for(Class c:cl.keySet())
		{
			ArrayList<String[]> classdata = cl.get(c);
			for(String[] s2: classdata)
			{
				if(s2[1].equals(""))
					s2[1] = "0";
				if(s2[1].equals("NC"))
					continue;

				if(Integer.valueOf(s2[0]) < firstSemester || Integer.valueOf(s2[0]) > lastSemester)
					continue;

				grade += Integer.valueOf(s2[1]) * c.getWeight();
				weight += c.getWeight();
			}
		}

		cl = this.classes.get(ClassStatus.REPROVED_FREQUENCY);
		if(cl != null) for(Class c: cl.keySet())
		{	
			ArrayList<String[]> classdata = cl.get(c);
			for(String[] s2: classdata)
			{
				if(Integer.valueOf(s2[0]) < firstSemester || Integer.valueOf(s2[0]) > lastSemester)
					continue;

				weight += c.getWeight();
			}

		}
		return (float) grade / weight;
	}
	
	public void addClass(String _class, String semester, ClassStatus status, String grade, String weight) {
		
		// A linha abaixo � necess�ria por conta das equival�ncias de disciplinas
		Class _class2 = ClassFactory.getClass(_class);
		
		if (!this.classes.containsKey(status))
			this.classes.put(status, new HashMap<Class, ArrayList<String[]>>());
		
		ArrayList<String[]> a = this.classes.get(status).get(_class2);
		if(a == null) {
			a = new ArrayList<String[]>();
		}
		
		//Adiciona os creditos da disciplina
		_class2.setWeight(Integer.valueOf(weight));
		
		String[] o = new String[2]; //Array que guarda as informacoes de cada vez que o aluno fez a disciplina (Semestre, Disciplina...)
		
		o[0] = semester;//adiciona o semestre cursado
		o[1] = grade; //adiciona a nota da disciplina
		a.add(o);
		this.classes.get(status).put(_class2, a);
		
		int sem = Integer.valueOf(semester);
		
		if(sem < this._firstSemester || this._firstSemester == 0)
			this._firstSemester = sem;
	}
	
	public String getId() { return this._id; }
	
	public int getFirstSemester() { return this._firstSemester; }
	
	@Override
	public String toString() {
		String output = "Student " + this._id;

		for (ClassStatus status : this.classes.keySet()) {
			output += ", "+status.name()+"=";
			for (Object string : this.classes.get(status).keySet().toArray()) 
				output += "," + string;
			
		}
		return output;
	}
	
	public HashMap<Class, ArrayList<String[]>> getClasses(ClassStatus cs) {
		if (!this.classes.containsKey(cs))
			this.classes.put(cs, new HashMap<Class, ArrayList<String[]>>());

		return classes.get(cs);
	}
		
	@Override
	public int compareTo(Student s)
	{
		return s._id.compareTo(this._id);
	}
		
}