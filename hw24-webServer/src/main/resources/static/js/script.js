
function btnAddClient( e ){
    let authorized = e.getAttribute( 'data-authorized' );
    if ( authorized === 'true' ){
        window.location.href = '/clients/form';
        return;
    }
    let toLoginForm = confirm("Для того чтобы иметь возможность добавлять клиента вы должны быть авторихованы. Хотите продолжить?");
    if( toLoginForm ){
        window.location.href = '/login';
    }
    return;
}

function postClientForm(){
    let messageErrorBlock = document.getElementById('message-error' );
    messageErrorBlock.classList.add( 'display-none' );
    messageErrorBlock.innerText = '';

    let client = document.getElementById('client' )?.value.trim() ?? '';
    let address = document.getElementById('address' )?.value.trim() ?? ''
    let phones = document.getElementById('phones' )?.value.trim() ?? '';


    if ( client === '' || address === '' || phones === '' ){
        let messageErrorBlock = document.getElementById('message-error' );
        messageErrorBlock.innerText = 'Все поля формы обязательны для заполнения';
        messageErrorBlock.classList.remove('display-none' );
        window.scrollTo({ top: 0, behavior: 'smooth' });
        return false;
    }

    document.getElementById('client-form' )
        .submit();
}