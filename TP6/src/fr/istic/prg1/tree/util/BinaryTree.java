package fr.istic.prg1.tree.util;

import java.util.Stack;

/**
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @version 4.0
 * @since 2015-06-15
 * @param <T>
 *            type formel d'objet pour la classe
 *
 *            Les arbres binaires sont construits par chaînage par références
 *            pour les fils et une pile de pères.
 */
public class BinaryTree<T> {

	/**
	 * Type représentant les noeuds.
	 */
	private class Element {
		public T value;
		public Element left, right;

		public Element() {
			value = null;
			left = null;
			right = null;
		}

		public boolean isEmpty() {
			return left == null && right == null;
		}
	}

	private Element root;

	public BinaryTree() {
		root = new Element();
	}

	/**
	 * @return Un nouvel iterateur sur l'arbre this. Le noeud courant de
	 *         l’itérateur est positionné sur la racine de l’arbre.
	 */
	public TreeIterator iterator() {
	    return new TreeIterator();
	}

	/**
	 * @return true si l'arbre this est vide, false sinon
	 */
	public boolean isEmpty() {
		return this.root.isEmpty();
	}

	/**
	 * Classe représentant les itérateurs sur les arbres binaires.
	 */
	public class TreeIterator implements Iterator<T> {
		private Element currentNode;
		private Stack<Element> stack;

		private TreeIterator() {
			stack = new Stack<Element>();
			currentNode = root;
		}

		/**
		 * L'itérateur se positionnne sur le fils gauche du noeud courant.
		 * 
		 * @pre Le noeud courant n’est pas un butoir.
		 */
		@Override
		public void goLeft() {
			try {
				assert !this.isEmpty() : "le butoir n'a pas de fils";
				
				//On va sur le fils gauche
				this.stack.push(this.currentNode);
				this.currentNode = this.currentNode.left;
			} catch (AssertionError e) {
				e.printStackTrace();
				System.exit(0);
			}
		}

		/**
		 * L'itérateur se positionnne sur le fils droit du noeud courant.
		 * 
		 * @pre Le noeud courant n’est pas un butoir.
		 */
		@Override
		public void goRight() {
			try {
				assert !this.isEmpty() : "le butoir n'a pas de fils";
				
				//On va sur le fils droit
				this.stack.push(this.currentNode);
				this.currentNode = this.currentNode.right;
			} catch (AssertionError e) {
				e.printStackTrace();
				System.exit(0);
			}
		}

		/**
		 * L'itérateur se positionnne sur le père du noeud courant.
		 * 
		 * @pre Le noeud courant n’est pas la racine.
		 */
		@Override
		public void goUp() {
			try {
				assert !stack.empty() : " la racine n'a pas de pere";
				
				//On sort le premier élément de la pile
				this.currentNode = this.stack.pop();
			} catch (AssertionError e) {
				e.printStackTrace();
				System.exit(0);
			}
		}

		/**
		 * L'itérateur se positionne sur la racine de l'arbre.
		 */
		@Override
		public void goRoot() {
			this.currentNode = BinaryTree.this.root;
			while (!stack.empty()) stack.pop();
		}

		/**
		 * @return true si l'iterateur est sur un sous-arbre vide, false sinon
		 */
		@Override
		public boolean isEmpty() {
			//Si on est sur un noeud butoir alors il n'y a aucun fils donc noeud vide
		    return this.currentNode.isEmpty();
		}

		/**
		 * @return Le genre du noeud courant.
		 */
		@Override
		public NodeType nodeType() {
			if (this.currentNode.isEmpty()) return NodeType.SENTINEL;
			if (this.currentNode.left.value == null) return NodeType.LEAF;
			return NodeType.DOUBLE;
		}

		/**
		 * Supprimer le noeud courant de l'arbre.
		 * 
		 * @pre Le noeud courant n'est pas un noeud double.
		 */
		@Override
		public void remove() {
			try {
				assert this.nodeType() != NodeType.DOUBLE : "retirer : retrait d'un noeud double non permis";
				
				this.clear();
			} catch (AssertionError e) {
				e.printStackTrace();
				System.exit(0);
			}
		}

		/**
		 * Vider le sous–arbre référencé par le noeud courant, qui devient
		 * butoir.
		 */
		@Override
		public void clear() {
			
			if(!this.stack.empty()) {
				Element lastCurrent = this.currentNode;
				this.goUp();
				
				if(this.currentNode.left == lastCurrent) {
					this.currentNode.left = new Element();
					this.goLeft();
				} else {
					this.currentNode.right = new Element();
					this.goRight();
				}
			// cas de la racine
			} else {
				BinaryTree.this.root = new Element();
				this.goRoot();
			}
			
			
			
			/*Element lastCurrent = this.currentNode,
					current;
			
			if (stack.empty()) {
				BinaryTree.this.root = new Element();
			} else {
				this.currentNode = stack.pop();

				if (stack.empty()) {
					if (this.currentNode.left == lastCurrent) {
						BinaryTree.this.root = BinaryTree.this.root.right;
					} else
						BinaryTree.this.root = BinaryTree.this.root.left;
				} else {
					current = this.currentNode;
					this.currentNode = stack.pop();
					
					if(current.left == lastCurrent) {
						if (this.currentNode.left == current) {
							this.currentNode.left = current.right;
							this.goLeft();
						} else {
							this.currentNode.right = current.right;
							this.goRight();
						}
						
					} else {
						if (this.currentNode.left == current) {
							this.currentNode.left = current.left;
							this.goLeft();
						} else { 
							this.currentNode.right = current.left;	
							this.goRight();
						}
					}
				}
			}*/
		}

		/**
		 * @return La valeur du noeud courant.
		 */
		@Override
		public T getValue() {
		    return this.currentNode.value;
		}

		/**
		 * Créer un nouveau noeud de valeur v à cet endroit.
		 * 
		 * @pre Le noeud courant est un butoir.
		 * 
		 * @param v
		 *            Valeur à ajouter.
		 */

		@Override
		public void addValue(T v) {
			try {
				assert this.isEmpty() : "Ajouter : on n'est pas sur un butoir";
				
				this.currentNode.value = v;
				this.currentNode.left = new Element();
				this.currentNode.right = new Element();
				
			} catch (AssertionError e) {
				e.printStackTrace();
				System.exit(0);
			}
		}

		/**
		 * Affecter la valeur v au noeud courant.
		 * 
		 * @param v
		 *            La nouvelle valeur du noeud courant.
		 */
		@Override
		public void setValue(T v) {
			this.currentNode.value = v;
		}

		private void ancestor(int i, int j) {
			try {
				assert !stack.empty() : "switchValue : argument trop grand";
			} catch (AssertionError e) {
				e.printStackTrace();
				System.exit(0);
			}
			Element x = stack.pop();
			if (j < i) {
				ancestor(i, j + 1);
			} else {
				T v = x.value;
				x.value = currentNode.value;
				currentNode.value = v;
			}
			stack.push(x);
		}

		/**
		 * Échanger les valeurs associées au noeud courant et à son père d’ordre
		 * i (le noeud courant reste inchangé).
		 * 
		 * @pre i>= 0 et racine est père du noeud courant d’ordre >= i.
		 * 
		 * @param i
		 *            ordre du père
		 */
		@Override
		public void switchValue(int i) {
			try {
				assert i >= 0 : "switchValue : argument negatif";
			} catch (AssertionError e) {
				e.printStackTrace();
				System.exit(0);
			}
			if (i > 0) {
				ancestor(i, 1);
			}
		}
	}
}
