const API = "/api/projetos";

const formulario = document.getElementById("formProjeto");
const campoDescricao = document.getElementById("descricao");
const campoInicio = document.getElementById("dataInicio");
const campoFim = document.getElementById("dataFim");
const botaoSalvar = document.getElementById("btnSalvar");
const botaoAtualizar = document.getElementById("btnAtualizar");
const tabela = document.getElementById("tabelaProjetos");
const contador = document.getElementById("contador");
const estadoLista = document.getElementById("estadoLista");
const aviso = document.getElementById("aviso");

function mostrarAviso(mensagem, tipo) {
  aviso.textContent = mensagem;
  aviso.className = `aviso ${tipo}`;
  aviso.hidden = false;
}

function formatarData(data) {
  if (!data || !/^\d{4}-\d{2}-\d{2}$/.test(data)) return "-";
  const [ano, mes, dia] = data.split("-");
  return `${dia}/${mes}/${ano}`;
}

async function mensagemDeErro(resposta) {
  try {
    const dados = await resposta.json();
    if (Array.isArray(dados.errors) && dados.errors.length > 0) {
      return dados.errors.join(" ");
    }
    if (typeof dados.message === "string" && dados.message) return dados.message;
  } catch (_) {
    // Uma resposta de erro nem sempre contem JSON.
  }
  return `Erro HTTP ${resposta.status}.`;
}

function adicionarCelula(linha, valor) {
  const celula = document.createElement("td");
  celula.textContent = String(valor);
  linha.appendChild(celula);
}

async function carregarProjetos() {
  botaoAtualizar.disabled = true;
  contador.textContent = "Carregando...";
  estadoLista.textContent = "Carregando projetos...";
  estadoLista.hidden = false;

  try {
    const resposta = await fetch(API, { headers: { Accept: "application/json" } });
    if (!resposta.ok) throw new Error(await mensagemDeErro(resposta));

    const projetos = await resposta.json();
    if (!Array.isArray(projetos)) throw new Error("Resposta inesperada da API.");

    tabela.replaceChildren();
    for (const projeto of projetos) {
      const linha = document.createElement("tr");
      adicionarCelula(linha, projeto.id ?? "-");
      adicionarCelula(linha, projeto.descricao ?? "-");
      adicionarCelula(linha, formatarData(projeto.dataInicio));
      adicionarCelula(linha, formatarData(projeto.dataFim));
      adicionarCelula(linha, Array.isArray(projeto.funcionarios) ? projeto.funcionarios.length : 0);
      tabela.appendChild(linha);
    }

    contador.textContent = `${projetos.length} projeto(s) cadastrado(s)`;
    estadoLista.textContent = "Nenhum projeto cadastrado ainda.";
    estadoLista.hidden = projetos.length > 0;
  } catch (erro) {
    tabela.replaceChildren();
    contador.textContent = "Lista indisponivel";
    estadoLista.textContent = `N\u00e3o foi poss\u00edvel carregar os projetos: ${erro.message}`;
    estadoLista.hidden = false;
  } finally {
    botaoAtualizar.disabled = false;
  }
}

formulario.addEventListener("submit", async (evento) => {
  evento.preventDefault();
  aviso.hidden = true;

  const descricao = campoDescricao.value.trim();
  const dataInicio = campoInicio.value;
  const dataFim = campoFim.value;

  if (!descricao || !dataInicio || !dataFim) {
    mostrarAviso("Preencha todos os campos.", "erro");
    return;
  }
  if (dataFim < dataInicio) {
    mostrarAviso("A data de t\u00e9rmino n\u00e3o pode ser anterior \u00e0 data de in\u00edcio.", "erro");
    return;
  }

  botaoSalvar.disabled = true;
  botaoSalvar.textContent = "Cadastrando...";
  try {
    const resposta = await fetch(API, {
      method: "POST",
      headers: { "Content-Type": "application/json", Accept: "application/json" },
      body: JSON.stringify({ descricao, dataInicio, dataFim })
    });
    if (!resposta.ok) throw new Error(await mensagemDeErro(resposta));

    formulario.reset();
    mostrarAviso("Projeto cadastrado com sucesso!", "sucesso");
    await carregarProjetos();
  } catch (erro) {
    mostrarAviso(`N\u00e3o foi poss\u00edvel cadastrar o projeto: ${erro.message}`, "erro");
  } finally {
    botaoSalvar.disabled = false;
    botaoSalvar.textContent = "Cadastrar projeto";
  }
});

botaoAtualizar.addEventListener("click", carregarProjetos);
carregarProjetos();
