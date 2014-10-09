package br.ufjf.coordenacao.OfertaVagas.model;

import java.util.HashMap;

public class StudentsHistory {

	private HashMap<String, Student> _students = new HashMap<String, Student>();
	
	public StudentsHistory() { }
	
	public void add(String id, String nome, String semester, String _class, ClassStatus status) {
		
		Student st = this._students.get(id);
		
		if (st == null) {
			st = new Student(id);
			st.setNome(nome);
			this._students.put(id, st);
		}
		
		st.addClass(_class, semester, status);
	}

	public HashMap<String, Student> getStudents() {
		return this._students;
	}
	
	@Override
	public String toString() {
		
		String output = "";
		for (Student student : this._students.values()) {
			output += student.toString() + "\n";
		}
		
		return output;
	}
	
}
