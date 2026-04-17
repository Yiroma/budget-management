# ============================================================================
# Budget Management - Makefile
# ============================================================================

# --- Variables --------------------------------------------------------------

COMPOSE     = docker compose
COMPOSE_DEV = $(COMPOSE) -f docker-compose.yml
# TODO: créer docker-compose.prod.yml avec des builds multi-stage optimisés
COMPOSE_PROD = $(COMPOSE) -f docker-compose.yml

FRONTEND = budget-frontend
BACKEND  = budget-backend
DB       = budget-db

# --- Cible par défaut -------------------------------------------------------

.DEFAULT_GOAL := help

# --- Commandes --------------------------------------------------------------

.PHONY: help dev run build stop down restart test lint lint-fix format logs prune

help: ## Affiche cette aide
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'

dev: ## Lance l'environnement de développement
	$(COMPOSE_DEV) up --build

run: ## Lance l'environnement de production (détaché)
	$(COMPOSE_PROD) up --build -d

build: ## Build les images Docker sans lancer les containers
	$(COMPOSE_DEV) build

stop: ## Stoppe les containers sans les supprimer
	$(COMPOSE_DEV) stop

down: ## Stoppe et supprime les containers + networks
	$(COMPOSE_DEV) down

restart: ## Redémarre les containers
	$(COMPOSE_DEV) restart

test: ## Lance tous les tests (front + back)
	$(COMPOSE_DEV) exec $(BACKEND) ./mvnw test
	$(COMPOSE_DEV) exec $(FRONTEND) npm test

lint: ## Vérifie le linting frontend (ESLint)
	npm run lint

lint-fix: ## Corrige automatiquement les erreurs de lint frontend
	npm run lint:fix

format: ## Formate tous les fichiers (Prettier + Spotless)
	npm run format
	npm run java:format

logs: ## Affiche les logs de tous les containers (follow)
	$(COMPOSE_DEV) logs -f

prune: ## Supprime containers, images, volumes et builds du projet
	$(COMPOSE_DEV) down --rmi all --volumes --remove-orphans
